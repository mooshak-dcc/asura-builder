package pt.up.fc.dcc.asura.builder.base.movie;

import pt.up.fc.dcc.asura.builder.base.exceptions.BuilderException;
import pt.up.fc.dcc.asura.builder.base.exceptions.PlayerException;
import pt.up.fc.dcc.asura.builder.base.movie.models.MooshakClassification;
import pt.up.fc.dcc.asura.builder.base.movie.models.GameMovie;

import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * Interface implemented to game movie builders
 *
 * @author José Paulo Leal <code>zp@dcc.fc.up.pt</code>
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public interface GameMovieBuilder {
    Logger LOGGER = Logger.getLogger(GameMovieBuilder.class.getSimpleName());

    /**
     * Set the title of the movie
     *
     * @param title title of the movie
     */
    void setTitle(String title);

    /**
     * Set the background URL for the movie
     *
     * @param backgroundURL URL or relative path from game assets to the background
     */
    void setBackground(String backgroundURL);

    /**
     * Set the width of the movie
     *
     * @param width the width of the movie
     */
    void setWidth(int width);

    /**
     * Set the height of the movie
     *
     * @param height the height of the movie
     */
    void setHeight(int height);

    /**
     * Add a sprite to the movie
     *
     * @param name Name of the sprite
     * @param URL  URL or relative path from game assets to the sprite
     */
    void addSprite(String name, String URL);

    /**
     * Add a player to the list
     *
     * @param id   player identifier
     * @param name player name
     */
    void addPlayer(String id, String name);

    /**
     * Set the number of frames-per-second
     *
     * @param fps the number of frames-per-second
     */
    void setFps(int fps);

    /**
     * Set the point of the sprite to which the coordinates are relative to
     *
     * @param anchor the point of the sprite to which the coordinates are relative
     */
    void setSpriteAnchor(SpriteAnchor anchor);

    /**
     * Add frame to game movie
     */
    void addFrame();

    /**
     * Add sprite item to current frame
     *
     * @param sprite sprite identifier
     * @param x      horizontal position
     * @param y      vertical position
     */
    void addItem(String sprite, int x, int y);

    /**
     * Add sprite item to current frame with rotation
     *
     * @param sprite sprite identifier
     * @param x      horizontal position
     * @param y      vertical position
     * @param rotate rotation of the sprite (default 0)
     */
    void addItem(String sprite, int x, int y, double rotate);

    /**
     * Add sprite item to current frame with rotation and scaling
     *
     * @param sprite sprite identifier
     * @param x      horizontal position
     * @param y      vertical position
     * @param rotate rotation of the sprite (default 0)
     * @param scale  scale to draw object (default is 1)
     */
    void addItem(String sprite, int x, int y, double rotate, double scale);

    /**
     * Add sprite item to current frame with view window
     *
     * @param sprite sprite identifier
     * @param x      horizontal position
     * @param y      vertical position
     * @param startX the x coordinate of the upper-left corner of the view window
     * @param startY the y coordinate of the upper-left corner of the view window
     * @param width  the width of the view window
     * @param height the height of the view window
     */
    void addItem(String sprite, int x, int y, int startX, int startY, int width, int height);

    /**
     * Add sprite item to current frame with rotation and scaling and view window
     *
     * @param sprite sprite identifier
     * @param x      horizontal position
     * @param y      vertical position
     * @param rotate rotation of the sprite (default 0)
     * @param scale  scale to draw object (default is 1)
     * @param startX the x coordinate of the upper-left corner of the view window
     * @param startY the y coordinate of the upper-left corner of the view window
     * @param width  the width of the view window
     * @param height the height of the view window
     */
    void addItem(String sprite, int x, int y, double rotate, double scale,
                 int startX, int startY, int width, int height);

    /**
     * Add message to the current frame for a given player
     *
     * @param player  player to which the message is sent
     * @param message message to display
     */
    void addMessage(String player, String message);

    /**
     * Set the points of the player
     *
     * @param player player to which the points belong
     * @param points points of the player
     */
    void setPoints(String player, int points);

    /**
     * Set the classification of the player
     *
     * @param player         player to which the classification belong
     * @param classification {@link MooshakClassification} classification of the player
     */
    void setClassification(String player, MooshakClassification classification);

    /**
     * Set the classification of the player
     *
     * @param player       player to which the observations belong
     * @param observations observations to the player
     */
    void setObservations(String player, String observations);

    /**
     * Convenience method to set wrong answer to player with message
     *
     * @param player  player which got Wrong Answer
     * @param message message to the player
     */
    void wrongAnswer(String player, String message);

    /**
     * Convenience method that sets the status of every player to REQUIRES_REEVALUATION
     * when the evaluation fails due to an error not related with a specific player
     *
     * @param e {@link BuilderException} exception that caused the failure
     */
    void failedEvaluation(BuilderException e);

    /**
     * Convenience method that sets the status of a player when the evaluation fails due
     * to an error with a specific player. The status of the erroneous player is the
     * specified by the exception.
     *
     * @param e {@link PlayerException} exception that caused the failure
     */
    void failedEvaluation(PlayerException e);

    /**
     * Pushes the current frame into the stack
     */
    void saveFrame();

    /**
     * Pushes the current frame into the stack
     *
     * @param status   save status?
     * @param messages save messages?
     */
    void saveFrame(boolean status, boolean messages);

    /**
     * Pops the last saved frame from the stack
     */
    void restoreFrame();

    /**
     * Get the movie of the game
     *
     * @return {@link GameMovie} the movie of the game
     */
    GameMovie getMovie();

    /**
     * Write JSON game movie data to a stream
     *
     * @param stream output stream which will receive data
     */
    void toFile(OutputStream stream);

    /**
     * Build JSON string of the game movie
     *
     * @return JSON string of the game movie
     */
    String toString();

    /**
     * Position of the sprite to which the coordinates are relative
     */
    enum SpriteAnchor {
        CENTER, TOP, TOP_LEFT, TOP_RIGHT, BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT, LEFT, RIGHT
    }
}
