package pt.up.fc.dcc.asura.builder.base;

import pt.up.fc.dcc.asura.builder.base.messaging.PlayerAction;
import pt.up.fc.dcc.asura.builder.base.messaging.StateUpdate;
import pt.up.fc.dcc.asura.builder.base.movie.GameMovieBuilder;
import pt.up.fc.dcc.asura.builder.base.movie.models.GameMovie;
import pt.up.fc.dcc.asura.builder.base.movie.models.GamePlayerStatus;
import pt.up.fc.dcc.asura.builder.base.utils.Json;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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
     * @param state   current instance (depends on game)
     * @param players map of players' processes (for the same game) keyed by player
     *                id
     * @throws IOException            - Any of the usual Input/Output related exceptions
     * @throws ClassNotFoundException -
     */
    public abstract void manage(GameState state, Map<String, Process> players)
            throws IOException, ClassNotFoundException;

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
        return movieBuilder.toString();
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
     * Aggregate of all input and output object streams keyed by process.
     * Instances of this class take care of opening and closing streams, as well
     * as sending {@code GameState} and receiving {@code PlayerAction} for each
     * process associated with a player
     */
    public static class Streamer implements Closeable {
        private Map<String, BufferedWriter> outs = new HashMap<>();
        private Map<String, BufferedReader> ins = new HashMap<>();

        public Streamer(Map<String, Process> processes) {
            for (String player : processes.keySet()) {
                Process process = processes.get(player);
                outs.put(player, new BufferedWriter(new OutputStreamWriter(process.getOutputStream())));
                ins.put(player, new BufferedReader(new InputStreamReader(process.getInputStream())));
            }
        }

        /**
         * Send an update of the state of the game to a player
         *
         * @param player      Player to receive updates
         * @param stateUpdate {@link StateUpdate} Update of the state of the game
         * @throws IOException - If an error occurs while writing to the player
         *                     InputStream
         */
        public void sendStateUpdateTo(String player, StateUpdate stateUpdate) throws IOException {

            if (stateUpdate == null)
                stateUpdate = new StateUpdate(null, null);

            String json = Json.get().objectToString(stateUpdate);
            outs.get(player).write(json);
            outs.get(player).newLine();
            outs.get(player).flush();
        }

        /**
         * Read the action from a player
         *
         * @param player Player that is sending updates
         * @return {@link PlayerAction} action from a player
         * @throws IOException - Any of the usual Input/Output related exceptions
         */
        public PlayerAction readActionFrom(String player) throws IOException {
            String json = ins.get(player).readLine();
            return Json.get().objectFromString(json, PlayerAction.class);
        }

        @Override
        public void close() throws IOException {
            for (String player : ins.keySet()) {
                ins.get(player).close();
            }

            for (String player : outs.keySet()) {
                outs.get(player).close();
            }
        }
    }

}
