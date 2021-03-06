package pt.up.fc.dcc.asura.builder.base;

import pt.up.fc.dcc.asura.builder.base.exceptions.BuilderException;
import pt.up.fc.dcc.asura.builder.base.exceptions.PlayerException;
import pt.up.fc.dcc.asura.builder.base.messaging.PlayerAction;
import pt.up.fc.dcc.asura.builder.base.messaging.StateUpdate;
import pt.up.fc.dcc.asura.builder.base.movie.GameMovieBuilder;
import pt.up.fc.dcc.asura.builder.base.movie.GameMovieBuilderImpl;
import pt.up.fc.dcc.asura.builder.base.movie.models.GameMovie;
import pt.up.fc.dcc.asura.builder.base.movie.models.GamePlayerStatus;
import pt.up.fc.dcc.asura.builder.base.movie.models.MooshakClassification;
import pt.up.fc.dcc.asura.builder.base.utils.Json;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

import static pt.up.fc.dcc.asura.builder.base.movie.GameMovieBuilder.Compression;

/**
 * Abstract class common to all game managers, the classes that control the
 * student automated players. Players are passed as a list of processes in the
 * argument of method {@code manage()}.
 *
 * @author José Paulo Leal <code>zp@dcc.fc.up.pt</code>
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public abstract class GameManager {
    protected static final Logger LOGGER = Logger.getLogger(GameManager.class.getName());

    protected GameMovieBuilder movieBuilder = null;

    /**
     * Get the name of the game
     *
     * @return name of the game
     */
    public String getGameName() {
        return getClass().getSimpleName().replace("Manager", "");
    }

    /**
     * Get minimum players per match
     *
     * @return minimum players per match
     */
    public int getMinPlayersPerMatch() {
        return 1;
    }

    /**
     * Get maximum players per match
     *
     * @return maximum players per match
     */
    public int getMaxPlayersPerMatch() {
        return Integer.MAX_VALUE;
    }

    /**
     * Get the name of the class that implements {@code GameState} for this game
     *
     * @return name of the class that implements {@code GameState} for this game
     */
    public abstract String getGameStateClassName();

    /**
     * Executes a game with a list of players identified by their processes
     *
     * @param players map of players' processes (for the same game) keyed by player
     *                id
     */
    public final void manage(Map<String, Process> players) {

        movieBuilder = new GameMovieBuilderImpl();

        GameState gameState;
        try {
            gameState = initializeGameState();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {

            for (String playerId: players.keySet())
                movieBuilder.addPlayer(playerId, null);

            movieBuilder.failedEvaluation(new BuilderException(e.getMessage()));
            return;
        }

        try {
            manage(gameState, players);
        } catch (PlayerException e) {
            movieBuilder.failedEvaluation(e);
        } catch (BuilderException e) {
            movieBuilder.failedEvaluation(e);
        }
    }

    /**
     * Executes a game with a list of players identified by their processes and an
     * initial state
     *
     * @param state   current instance (depends on game)
     * @param players map of players' processes (for the same game) keyed by player
     *                id
     * @throws BuilderException - If an exception occurs related to the game manager, game state, or in the builder framework
     * @throws PlayerException - If an exception occurs related to a player behavior
     */
    protected abstract void manage(GameState state, Map<String, Process> players)
            throws BuilderException, PlayerException;

    /**
     * Get evaluation status of the player
     *
     * @param player Player to get status from
     * @return {@link GamePlayerStatus} evaluation status of a player
     */
    public GamePlayerStatus getGamePlayerStatus(String player) {

        GameMovie movie = movieBuilder.getMovie();

        if (movie == null || movie.getFrames().isEmpty())
            return null;

        final GamePlayerStatus status = movie.getFrames()
                .get(movie.getFrames().size() - 1)
                .getStatus(player);

        if (status == null)
            return null;

        return status;
    }

    /**
     * Get movie of the game execution
     *
     * @return {@code GameMovie} game movie
     */
    public GameMovie getGameMovie() {
        return movieBuilder.getMovie();
    }

    /**
     * Get movie of the game execution as string
     *
     * @return String with game movie
     */
    public String getGameMovieString() {
        return getGameMovieString(Compression.NONE.toString());
    }

    /**
     * Get movie of the game execution as string
     *
     * @param compression {@link String} algorithm to use for compression
     * @return String with game movie
     */
    public String getGameMovieString(String compression) {
        return movieBuilder.toString(Compression.valueOf(compression));
    }

    /**
     * Export game movie to an output stream
     *
     * @param outputStream {@link OutputStream} stream to write game movie
     */
    public void exportGameMovie(OutputStream outputStream) {
        movieBuilder.toFile(outputStream);
    }

    /**
     * Get name from initial state update
     *
     * @param action {@link PlayerAction} Initial state update
     * @return name Name of the player
     */
    protected String getName(PlayerAction action) {
        if (action == null)
            throw new IllegalArgumentException("update expected");
        else if (action.getCommand().getArgs().length == 0)
            throw new IllegalArgumentException("no arguments in initial command");
        else if (!(action.getCommand().getArgs()[0] instanceof String))
            throw new IllegalArgumentException("argument in initial command not a string");
        else
            return (String) action.getCommand().getArgs()[0];
    }

    /**
     * Instantiates a {@link GameState} from a given binary name
     *
     * @return instance of {@link GameState}
     * @throws InstantiationException - Failed to get an instance of the game state
     * @throws IllegalAccessException - Illegal access to class instance
     * @throws ClassNotFoundException - State class was not found
     */
    private GameState initializeGameState()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        String stateClassName = getGameStateClassName();

        ClassLoader loader = getClass().getClassLoader();
        Class<?> clazz = loader.loadClass(stateClassName);
        Object object = clazz.newInstance();

        if (object instanceof GameState)
            return (GameState) object;
        else
            throw new IllegalArgumentException("Not a state class: " + stateClassName);
    }

    /**
     * Aggregate of all input and output object streams keyed by process.
     * Instances of this class take care of opening and closing streams, as well
     * as sending {@code GameState} and receiving {@code PlayerAction} for each
     * process associated with a player
     */
    public static class Streamer implements Closeable {
        private Map<String, Process> processes = new HashMap<>();
        private Map<String, BufferedWriter> outs = new HashMap<>();
        private Map<String, BufferedReader> ins = new HashMap<>();
        private ExecutorService executorService = Executors.newSingleThreadExecutor();

        public Streamer(Map<String, Process> processes) {
            for (String player : processes.keySet()) {
                Process process = processes.get(player);
                this.processes.put(player, process);
                outs.put(player, new BufferedWriter(new OutputStreamWriter(process.getOutputStream())));
                ins.put(player, new BufferedReader(new InputStreamReader(process.getInputStream())));
            }
        }

        /**
         * Send an update of the state of the game to a player
         *
         * @param player      Player to receive updates
         * @param stateUpdate {@link StateUpdate} Update of the state of the game
         * @throws PlayerException - If an error occurs while writing to the player
         *                     InputStream
         */
        public void sendStateUpdateTo(String player, StateUpdate stateUpdate) throws PlayerException {

            if (stateUpdate == null)
                stateUpdate = new StateUpdate(null, null);

            String json = Json.get().objectToString(stateUpdate);

            try {
                outs.get(player).write(json);
                outs.get(player).newLine();
                outs.get(player).flush();
            } catch (IOException e) {
                throw new PlayerException(player, MooshakClassification.RUNTIME_ERROR,
                        "The Game Manager could not send the update to you! Maybe your stream was closed" +
                                " before the match ended!");
            }
        }

        /**
         * Read the action from a player
         *
         * @param player ID of the player that is sending updates
         * @return {@link PlayerAction} action from a player
         * @throws PlayerException - If there is an error understanding the action
         */
        public PlayerAction readActionFrom(String player) throws PlayerException {

            PlayerAction action;
            try {
                String json = ins.get(player).readLine();

                action = Json.get().objectFromString(json, PlayerAction.class);

                if (action == null)
                    throw new Exception();
            } catch (Exception e) {
                throw new PlayerException(player, MooshakClassification.RUNTIME_ERROR,
                        "Your action could not be parsed by the Game Manager!");
            }

            return action;
        }

        /**
         * Read the action from a player with a specific timeout
         *
         * @param player ID of the player that is sending updates
         * @param timeout Timeout for reading actions
         * @return {@link PlayerAction} action from a player
         * @throws PlayerException - If there is an error understanding the action
         */
        public PlayerAction readActionWithTimeoutFrom(String player, long timeout) throws PlayerException {

            Future<PlayerAction> future = executorService.submit(() -> readActionFrom(player));

            try {
                return future.get(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException e) {
                throw new PlayerException(player, MooshakClassification.RUNTIME_ERROR, e.getMessage(), e);
            } catch (TimeoutException e) {
                future.cancel(true);
                throw new PlayerException(player, MooshakClassification.TIME_LIMIT_EXCEEDED);
            }
        }

        @Override
        public void close() throws IOException {
            for (String player : ins.keySet()) {
                processes.get(player).getInputStream().close();
                //ins.get(player).close();
            }

            for (String player : outs.keySet()) {
                processes.get(player).getOutputStream().close();
                //outs.get(player).close();
            }

            executorService.shutdownNow();
        }
    }

}
