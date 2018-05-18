package pt.up.fc.dcc.asura.builder.base.utils;

import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Test;
import pt.up.fc.dcc.asura.builder.base.messaging.StateUpdate;
import pt.up.fc.dcc.asura.builder.base.movie.GameMovieBuilder;
import pt.up.fc.dcc.asura.builder.base.movie.models.GameFrameItem;
import pt.up.fc.dcc.asura.builder.base.movie.models.GameMovie;

import java.io.*;
import java.util.Collection;

/**
 * Test JSON utilities
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class JsonUtilsTest {

    private static final String JSON_MATCH_MOVIE = "{\"header\":{\"title\":\"Tic Tac Toe Game\",\"background\":\"board-3x3.png\",\"width\":600,\"height\":600,\"sprites\":{\"o_piece\":\"o_piece.png\",\"x_piece\":\"x_piece.png\"},\"players\":{\"jcpaiva\":\"Random\",\"zp\":\"Random\"},\"anchor_point\":\"TOP_LEFT\",\"fps\":1},\"frames\":[{\"items\":[{\"sprite\":\"x_piece\",\"x\":0,\"y\":187}],\"status\":{},\"messages\":{}},{\"items\":[{\"sprite\":\"o_piece\",\"x\":374,\"y\":0},{\"sprite\":\"x_piece\",\"x\":0,\"y\":187}],\"status\":{},\"messages\":{}},{\"items\":[{\"sprite\":\"o_piece\",\"x\":374,\"y\":0},{\"sprite\":\"x_piece\",\"x\":0,\"y\":187},{\"sprite\":\"x_piece\",\"x\":187,\"y\":187}],\"status\":{},\"messages\":{}},{\"items\":[{\"sprite\":\"o_piece\",\"x\":187,\"y\":0},{\"sprite\":\"o_piece\",\"x\":374,\"y\":0},{\"sprite\":\"x_piece\",\"x\":0,\"y\":187},{\"sprite\":\"x_piece\",\"x\":187,\"y\":187}],\"status\":{},\"messages\":{}},{\"items\":[{\"sprite\":\"o_piece\",\"x\":187,\"y\":0},{\"sprite\":\"o_piece\",\"x\":374,\"y\":0},{\"sprite\":\"x_piece\",\"x\":0,\"y\":187},{\"sprite\":\"x_piece\",\"x\":187,\"y\":187},{\"sprite\":\"x_piece\",\"x\":0,\"y\":374}],\"status\":{},\"messages\":{}},{\"items\":[{\"sprite\":\"o_piece\",\"x\":187,\"y\":0},{\"sprite\":\"o_piece\",\"x\":374,\"y\":0},{\"sprite\":\"x_piece\",\"x\":0,\"y\":187},{\"sprite\":\"x_piece\",\"x\":187,\"y\":187},{\"sprite\":\"o_piece\",\"x\":374,\"y\":187},{\"sprite\":\"x_piece\",\"x\":0,\"y\":374}],\"status\":{},\"messages\":{}},{\"items\":[{\"sprite\":\"o_piece\",\"x\":187,\"y\":0},{\"sprite\":\"o_piece\",\"x\":374,\"y\":0},{\"sprite\":\"x_piece\",\"x\":0,\"y\":187},{\"sprite\":\"x_piece\",\"x\":187,\"y\":187},{\"sprite\":\"o_piece\",\"x\":374,\"y\":187},{\"sprite\":\"x_piece\",\"x\":0,\"y\":374},{\"sprite\":\"x_piece\",\"x\":187,\"y\":374}],\"status\":{},\"messages\":{}},{\"items\":[{\"sprite\":\"o_piece\",\"x\":187,\"y\":0},{\"sprite\":\"o_piece\",\"x\":374,\"y\":0},{\"sprite\":\"x_piece\",\"x\":0,\"y\":187},{\"sprite\":\"x_piece\",\"x\":187,\"y\":187},{\"sprite\":\"o_piece\",\"x\":374,\"y\":187},{\"sprite\":\"x_piece\",\"x\":0,\"y\":374},{\"sprite\":\"x_piece\",\"x\":187,\"y\":374},{\"sprite\":\"o_piece\",\"x\":374,\"y\":374}],\"status\":{},\"messages\":{\"jcpaiva\":\"The bot seems ok, but it has lost!\",\"zp\":\"You\\u0027ve won!\"}},{\"items\":[{\"sprite\":\"o_piece\",\"x\":187,\"y\":0},{\"sprite\":\"o_piece\",\"x\":374,\"y\":0},{\"sprite\":\"x_piece\",\"x\":0,\"y\":187},{\"sprite\":\"x_piece\",\"x\":187,\"y\":187},{\"sprite\":\"o_piece\",\"x\":374,\"y\":187},{\"sprite\":\"x_piece\",\"x\":0,\"y\":374},{\"sprite\":\"x_piece\",\"x\":187,\"y\":374},{\"sprite\":\"o_piece\",\"x\":374,\"y\":374}],\"status\":{\"jcpaiva\":{\"points\":0,\"classification\":\"Accepted\",\"observations\":\"The bot seems ok, but it has lost!\"},\"zp\":{\"points\":100,\"classification\":\"Accepted\",\"observations\":\"You\\u0027ve won!\"}},\"messages\":{}}]}";
    private static final String JSON_COLLECTION = "[{\"sprite\":\"o_piece\",\"x\":374,\"y\":0},{\"sprite\":\"x_piece\",\"x\":0,\"y\":187},{\"sprite\":\"x_piece\",\"x\":187,\"y\":187}]";

    @Test
    public void testObjectFromString() {
        GameMovie gameMovie = Json.get().objectFromString(JSON_MATCH_MOVIE, GameMovie.class);

        Assert.assertEquals(1, gameMovie.getHeader().getFps());
        Assert.assertEquals(600, gameMovie.getHeader().getHeight());
        Assert.assertEquals(600, gameMovie.getHeader().getWidth());
        Assert.assertEquals(GameMovieBuilder.SpriteAnchor.TOP_LEFT, gameMovie.getHeader().getAnchorPoint());
        Assert.assertEquals("board-3x3.png", gameMovie.getHeader().getBackground());
        Assert.assertEquals("Tic Tac Toe Game", gameMovie.getHeader().getTitle());
        Assert.assertEquals(2, gameMovie.getHeader().getSprites().keySet().size());
        Assert.assertEquals(2, gameMovie.getHeader().getPlayers().keySet().size());
        Assert.assertEquals(9, gameMovie.getFrames().size());
        Assert.assertEquals(6, gameMovie.getFrames().get(5).getItems().size());
    }

    @Test
    public void testJsonFromString() {
        JsonElement jsonElement = Json.get().jsonFromString(JSON_MATCH_MOVIE);

        Assert.assertEquals(1, jsonElement.getAsJsonObject().get("header").getAsJsonObject().get("fps")
                .getAsInt());
        Assert.assertEquals(600, jsonElement.getAsJsonObject().get("header").getAsJsonObject().get("height")
                .getAsInt());
        Assert.assertEquals(600, jsonElement.getAsJsonObject().get("header").getAsJsonObject().get("width")
                .getAsInt());
        Assert.assertEquals(GameMovieBuilder.SpriteAnchor.TOP_LEFT.toString(), jsonElement.getAsJsonObject()
                .get("header").getAsJsonObject().get("anchor_point").getAsString());
        Assert.assertEquals("board-3x3.png", jsonElement.getAsJsonObject().get("header").getAsJsonObject()
                .get("background").getAsString());
        Assert.assertEquals("Tic Tac Toe Game", jsonElement.getAsJsonObject().get("header").getAsJsonObject()
                .get("title").getAsString());
        Assert.assertEquals(2, jsonElement.getAsJsonObject().get("header").getAsJsonObject().get("sprites")
                .getAsJsonObject().size());
        Assert.assertEquals(2, jsonElement.getAsJsonObject().get("header").getAsJsonObject().get("players")
                .getAsJsonObject().size());
        Assert.assertEquals(9, jsonElement.getAsJsonObject().get("frames").getAsJsonArray().size());
        Assert.assertEquals(6, jsonElement.getAsJsonObject().get("frames").getAsJsonArray().get(5)
                .getAsJsonObject().getAsJsonArray("items").size());
    }

    @Test
    public void testCollectionFromString() {

        Collection<GameFrameItem> items = Json.get().collectionFromString(JSON_COLLECTION);

        Assert.assertEquals(3, items.size());
    }

    @Test
    public void testJsonInputOutputStream() throws IOException {

        final PipedOutputStream output = new PipedOutputStream();
        final PipedInputStream input = new PipedInputStream(output);

        final DataInputStream dis = new DataInputStream(input);
        final DataOutputStream dos = new DataOutputStream(output);

        Thread thread1 = new Thread(() -> {
            StateUpdate stateUpdate = new StateUpdate("CONN", null);

            String json = Json.get().objectToString(stateUpdate);
            try {
                dos.writeUTF(json);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                dos.writeUTF(json);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        Thread thread2 = new Thread(() -> {

            String json = null;
            try {
                json = dis.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }

            StateUpdate stateUpdate1 = Json.get().objectFromString(json, StateUpdate.class);
            Assert.assertEquals("CONN", stateUpdate1.getType());

            try {
                json = dis.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }

            StateUpdate stateUpdate2 = Json.get().objectFromString(json, StateUpdate.class);
            Assert.assertEquals("CONN", stateUpdate2.getType());
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
