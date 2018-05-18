package pt.up.fc.dcc.asura.builder.base.movie.models;

import pt.up.fc.dcc.asura.builder.base.utils.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Actual game movie
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class GameMovie extends JsonObject {

    private GameMovieHeader header = new GameMovieHeader();
    private List<GameMovieFrame> frames = new ArrayList<>();

    public GameMovie() {
    }

    public GameMovieHeader getHeader() {
        return header;
    }

    public void setHeader(GameMovieHeader header) {
        this.header = header;
    }

    public List<GameMovieFrame> getFrames() {
        return frames;
    }

    public void setFrames(List<GameMovieFrame> frames) {
        this.frames = frames;
    }
}
