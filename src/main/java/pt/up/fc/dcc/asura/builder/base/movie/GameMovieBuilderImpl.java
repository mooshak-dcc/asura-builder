package pt.up.fc.dcc.asura.builder.base.movie;

import pt.up.fc.dcc.asura.builder.base.exceptions.BuilderException;
import pt.up.fc.dcc.asura.builder.base.exceptions.PlayerException;
import pt.up.fc.dcc.asura.builder.base.movie.models.GamePlayerStatus;
import pt.up.fc.dcc.asura.builder.base.movie.models.MooshakClassification;
import pt.up.fc.dcc.asura.builder.base.movie.models.GameMovie;
import pt.up.fc.dcc.asura.builder.base.movie.models.GameMovieFrame;
import pt.up.fc.dcc.asura.builder.base.utils.CopyUtils;
import pt.up.fc.dcc.asura.builder.base.utils.Json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of game movie builder
 *
 * @author José Paulo Leal <code>zp@dcc.fc.up.pt</code>
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class GameMovieBuilderImpl implements GameMovieBuilder {

    private static final int STACK_SIZE = 15;

    private GameMovie movie = new GameMovie();
    private GameMovieFrame currentFrame;

    private LinkedList<GameMovieFrame> frameStack = new LinkedList<>();

    @Override
    public void setTitle(String title) {
        movie.getHeader().setTitle(title);
    }

    @Override
    public void setBackground(String background) {
        movie.getHeader().setBackground(background);
    }

    @Override
    public void setWidth(int width) {
        movie.getHeader().setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        movie.getHeader().setHeight(height);
    }

    @Override
    public void addSprite(String name, String url) {
        movie.getHeader().getSprites().put(name, url);
    }

    @Override
    public void addPlayer(String player, String name) {
        movie.getHeader().getPlayers().put(player, name);
    }

    @Override
    public void setFps(int fps) {
        movie.getHeader().setFps(fps);
    }

    @Override
    public void setSpriteAnchor(SpriteAnchor anchorPoint) {
        movie.getHeader().setAnchorPoint(anchorPoint);
    }

    @Override
    public void saveFrame() {
        saveFrame(true, true);
    }

    @Override
    public void saveFrame(boolean status, boolean messages) {

        GameMovieFrame frame = new GameMovieFrame();
        frame.setItems(new ArrayList<>(currentFrame.getItems()));

        if (status)
            frame.setStatus(CopyUtils.deepCopy(currentFrame.getStatus()));
        else
            frame.setStatus(null);

        if (messages)
            frame.setMessages(CopyUtils.deepCopy(currentFrame.getMessages()));
        else
            frame.setMessages(null);

        frameStack.addLast(frame);

        if (frameStack.size() > STACK_SIZE)
            frameStack.removeFirst();
    }

    @Override
    public void restoreFrame() {

        if (frameStack.isEmpty())
            return;

        GameMovieFrame frame = frameStack.removeLast();

        currentFrame.setItems(frame.getItems());

        if (frame.getStatus() != null)
            currentFrame.setStatus(frame.getStatus());

        if (frame.getMessages() != null)
            currentFrame.setMessages(frame.getMessages());
    }

    @Override
    public void addFrame() {
        currentFrame = new GameMovieFrame();
        movie.getFrames().add(currentFrame);
    }

    @Override
    public void addItem(String sprite, int x, int y) {
        currentFrame.addItem(sprite, x, y, null, null);
    }

    @Override
    public void addItem(String sprite, int x, int y, double rotate) {
        currentFrame.addItem(sprite, x, y, rotate, null);
    }

    @Override
    public void addItem(String sprite, int x, int y, double rotate, double scale) {
        currentFrame.addItem(sprite, x, y, rotate, scale);
    }

    @Override
    public void addMessage(String player, String message) {
        currentFrame.getMessages().put(player, message);
    }

    @Override
    public void setPoints(String player, int points) {
        currentFrame.getStatus(player).setPoints(points);
    }

    @Override
    public void setClassification(String player, MooshakClassification classification) {
        currentFrame.getStatus(player).setClassification(classification);
    }

    @Override
    public void setObservations(String player, String observations) {
        currentFrame.getStatus(player).setObservations(observations);
    }

    @Override
    public void wrongAnswer(String player, String message) {

        if (currentFrame == null)
            addFrame();

        GamePlayerStatus playerStatus = currentFrame.getStatus(player);
        playerStatus.setClassification(MooshakClassification.WRONG_ANSWER);
        playerStatus.setObservations(message);
        playerStatus.setPoints(0);
    }

    @Override
    public void failedEvaluation(BuilderException e) {

        if (currentFrame == null)
            addFrame();

        for (String playerId: movie.getHeader().getPlayers().keySet()) {

            GamePlayerStatus playerStatus = currentFrame.getStatus(playerId);
            playerStatus.setClassification(MooshakClassification.REQUIRES_REEVALUATION);
            playerStatus.setObservations(e.getMessage());
        }
    }

    @Override
    public void failedEvaluation(PlayerException e) {

        if (currentFrame == null)
            addFrame();

        GamePlayerStatus erroneousPlayerStatus = currentFrame.getStatus(e.getPlayerId());
        erroneousPlayerStatus.setClassification(e.getClassification());
        erroneousPlayerStatus.setPoints(0);
        erroneousPlayerStatus.setObservations(e.getMessage());

        for (String playerId: movie.getHeader().getPlayers().keySet()) {

            if (playerId.equals(e.getPlayerId()))
                continue;

            GamePlayerStatus playerStatus = currentFrame.getStatus(playerId);
            playerStatus.setClassification(MooshakClassification.REQUIRES_REEVALUATION);
            playerStatus.setObservations(e.getMessage());
        }
    }

    @Override
    public GameMovie getMovie() {
        return movie;
    }

    @Override
    public void toFile(OutputStream stream) {
        try {
            Json.get().writeToStream(movie, stream);
        } catch (IOException e) {
            throw new BuilderException("Error writing movie to stream: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return Json.get().objectToString(movie);
    }
}
