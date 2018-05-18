package pt.up.fc.dcc.asura.builder.base;

import pt.up.fc.dcc.asura.builder.base.messaging.PlayerAction;
import pt.up.fc.dcc.asura.builder.base.messaging.StateUpdate;
import pt.up.fc.dcc.asura.builder.base.movie.GameMovieBuilder;

import java.util.Map;

/**
 * An implementation of {@link GameState} is used to manage the game
 * through the methods.
 * <p>
 * <dl>
 * <dt><code>prepare(GameMovieBuilder,Set&lt;String&gt;)</code></dt>
 * <dd>to initialize the state</dd>
 * <p>
 * <dt><code>execute(String,GameUpdate)</code></dt>
 * <dd>to execute player's command to change the state</dd>
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
     * @param movieBuilder Game movie builder
     * @param players      Player names indexed by ID
     */
    void prepare(GameMovieBuilder movieBuilder, Map<String, String> players);

    /**
     * Changes game state according to update issued by a concrete player
     *
     * @param movieBuilder {@link GameMovieBuilder}
     *                     Game movie builder
     * @param playerId     Player identifier
     * @param action       {@link PlayerAction} Player action
     */
    void execute(GameMovieBuilder movieBuilder, String playerId, PlayerAction action);

    /**
     * Get an object to update player state
     *
     * @param player Player to update
     * @return state update to send to player
     */
    StateUpdate getStateUpdateFor(String player);

    /**
     * Called when all players' commands where executed in a round
     *
     * @param movieBuilder Game movie builder
     */
    void endRound(GameMovieBuilder movieBuilder);

    /**
     * Is the game still running?
     *
     * @return {@code true} if running; {@code false} otherwise
     */
    boolean isRunning();

    /**
     * Finalize the state. Run any necessary tasks when the game has ended.
     *
     * @param movieBuilder Game movie builder
     */
    void finalize(GameMovieBuilder movieBuilder);
}
