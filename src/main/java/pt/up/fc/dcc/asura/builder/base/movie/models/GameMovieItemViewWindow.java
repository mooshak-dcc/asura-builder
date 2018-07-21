package pt.up.fc.dcc.asura.builder.base.movie.models;

import pt.up.fc.dcc.asura.builder.base.utils.JsonObject;

/**
 * View window of a sprite of the game movie
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class GameMovieItemViewWindow extends JsonObject {
    private Integer startX;
    private Integer startY;
    private Integer width;
    private Integer height;

    public GameMovieItemViewWindow(Integer startX, Integer startY, Integer width, Integer height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    public Integer getStartX() {
        return startX;
    }

    public void setStartX(Integer startX) {
        this.startX = startX;
    }

    public Integer getStartY() {
        return startY;
    }

    public void setStartY(Integer startY) {
        this.startY = startY;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
