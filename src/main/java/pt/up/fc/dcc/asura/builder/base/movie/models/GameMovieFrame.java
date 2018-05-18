package pt.up.fc.dcc.asura.builder.base.movie.models;

import pt.up.fc.dcc.asura.builder.base.utils.JsonObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Frame of a game movie
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class GameMovieFrame extends JsonObject {
    private List<GameFrameItem> items = new ArrayList<>();
    private Map<String, GamePlayerStatus> status = new LinkedHashMap<>();
    private Map<String, String> messages = new LinkedHashMap<>();

    public GameMovieFrame() {
    }

    public List<GameFrameItem> getItems() {
        return items;
    }

    public void setItems(List<GameFrameItem> items) {
        this.items = items;
    }

    public Map<String, GamePlayerStatus> getStatus() {
        return status;
    }

    public void setStatus(Map<String, GamePlayerStatus> status) {
        this.status = status;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    /**
     * Add item to movie frame
     *
     * @param sprite Sprite identifier
     * @param x      Horizontal position
     * @param y      Vertical position
     * @param rotate Rotation of the sprite
     * @param scale  Scale to draw the sprite
     */
    public void addItem(String sprite, int x, int y, Double rotate, Double scale) {
        items.add(new GameFrameItem(sprite, x, y, rotate, scale));
    }

    /**
     * Get the status of a player
     *
     * @param player Player identifier
     * @return GamePlayerStatus describing the status of the player
     */
    public GamePlayerStatus getStatus(String player) {
        GamePlayerStatus status = this.status.get(player);

        if (status == null)
            this.status.put(player, status = new GamePlayerStatus());

        return status;
    }
}
