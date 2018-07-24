package pt.up.fc.dcc.asura.builder.base;

import pt.up.fc.dcc.asura.builder.base.exceptions.BuilderException;
import pt.up.fc.dcc.asura.builder.base.exceptions.PlayerException;
import pt.up.fc.dcc.asura.builder.base.messaging.PlayerAction;
import pt.up.fc.dcc.asura.builder.base.messaging.StateUpdate;
import pt.up.fc.dcc.asura.builder.base.movie.GameMovieBuilder;

import java.util.Map;

/**
 * An implementation of {@link GameState} is used to manage the game
 * through the methods.
 * <p>
 * <dl>
 * <dt><code>prepare(GameMovieBuilder,String,Map&lt;String,String&gt;)</code></dt>
 * <dd>to initialize the state</dd>
 * <p>
 * <dt><code>execute(GameMovieBuilder,String,PlayerAction)</code></dt>
 * <dd>to execute player's command to change the state</dd>
 * <p>
 * <dt><code>StateUpdate getStateUpdateFor(String)</code></dt>
 * <dd>get an object to update player state</dd>
 * <p>
 * <dt><code>endRound(GameMovieBuilder)</code></dt>
 * <dd>get new frame at the end of a round</dd>
 * <p>
 * <dt><code>isRunning()</code></dt>
 * <dd>check if game is still executing</dd>
 * <p>
 * <dt><code>finalize(GameMovieBuilder)</code></dt>
 * <dd>finalize the movie</dd>
 * <p>
 * </dl>
 *
 * @author José Paulo Leal <code>zp@dcc.fc.up.pt</code>
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public interface GameState {

    /**
     * Prepare state before the game starts
     *
     * @param movieBuilder {@link GameMovieBuilder} Game movie builder
     * @param title        {@link String} title of the movie
     * @param players      {@link Map} Player names indexed by ID
     * @throws BuilderException - If an exception occurs related to the game manager, game state, or in the builder framework
     * @throws PlayerException  - If an exception occurs related to a player behavior
     */
    void prepare(GameMovieBuilder movieBuilder, String title, Map<String, String> players) throws BuilderException, PlayerException;

    /**
     * Changes game state according to update issued by a concrete player
     *
     * @param movieBuilder {@link GameMovieBuilder} Game movie builder
     * @param playerId     {@link String} Player identifier
     * @param action       {@link PlayerAction} Player action
     * @throws BuilderException - If an exception occurs related to the game manager, game state, or in the builder framework
     * @throws PlayerException  - If an exception occurs related to a player behavior
     */
    void execute(GameMovieBuilder movieBuilder, String playerId, PlayerAction action) throws BuilderException, PlayerException;

    /**
     * Get an object to update player state
     *
     * @param player {@link String} Player to update
     * @return {@link StateUpdate} state update to send to player
     * @throws BuilderException - If an exception occurs related to the game manager, game state, or in the builder framework
     */
    StateUpdate getStateUpdateFor(String player) throws BuilderException;

    /**
     * Called when all players' commands where executed in a round
     *
     * @param movieBuilder {@link GameMovieBuilder} Game movie builder
     * @throws BuilderException - If an exception occurs related to the game manager, game state, or in the builder framework
     * @throws PlayerException  - If an exception occurs related to a player behavior
     */
    void endRound(GameMovieBuilder movieBuilder) throws BuilderException, PlayerException;

    /**
     * Is the game still running?
     *
     * @return {@code true} if running; {@code false} otherwise
     */
    boolean isRunning();

    /**
     * Finalize the state. Run any necessary tasks when the game has ended.
     *
     * @param movieBuilder {@link GameMovieBuilder} Game movie builder
     * @throws BuilderException - If an exception occurs related to the game manager, game state, or in the builder framework
     */
    void finalize(GameMovieBuilder movieBuilder) throws BuilderException;
}
