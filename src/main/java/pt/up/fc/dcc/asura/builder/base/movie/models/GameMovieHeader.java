package pt.up.fc.dcc.asura.builder.base.movie.models;

import pt.up.fc.dcc.asura.builder.base.movie.GameMovieBuilder;
import pt.up.fc.dcc.asura.builder.base.utils.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Header of the game movie
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class GameMovieHeader extends JsonObject {
    private String title;
    private String background;
    private int width;
    private int height;
    private Map<String, String> sprites = new LinkedHashMap<>();
    private Map<String, String> players = new LinkedHashMap<>();
    private GameMovieBuilder.SpriteAnchor anchorPoint = GameMovieBuilder.SpriteAnchor.CENTER;
    private int fps;

    public GameMovieHeader() {  }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Map<String, String> getSprites() {
        return sprites;
    }

    public void setSprites(Map<String, String> sprites) {
        this.sprites = sprites;
    }

    public Map<String, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, String> players) {
        this.players = players;
    }

    public GameMovieBuilder.SpriteAnchor getAnchorPoint() {
        return anchorPoint;
    }

    public void setAnchorPoint(GameMovieBuilder.SpriteAnchor anchorPoint) {
        this.anchorPoint = anchorPoint;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }
}
