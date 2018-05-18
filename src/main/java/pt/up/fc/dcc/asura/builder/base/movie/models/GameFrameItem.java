package pt.up.fc.dcc.asura.builder.base.movie.models;

import pt.up.fc.dcc.asura.builder.base.utils.JsonObject;

/**
 * Item of a game frame
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class GameFrameItem extends JsonObject {
    private String sprite;
    private int x, y;
    private Double rotate;
    private Double scale;

    public GameFrameItem(String sprite, int x, int y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public GameFrameItem(String sprite, int x, int y, Double rotate, Double scale) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.rotate = rotate;
        this.scale = scale;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Double getRotate() {
        return rotate;
    }

    public void setRotate(Double rotate) {
        this.rotate = rotate;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }
}
