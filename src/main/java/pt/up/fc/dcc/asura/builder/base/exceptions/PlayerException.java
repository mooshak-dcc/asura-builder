package pt.up.fc.dcc.asura.builder.base.exceptions;

import pt.up.fc.dcc.asura.builder.base.movie.models.MooshakClassification;

/**
 * Exception thrown by the Asura Builder component when the player
 * has an error in his/her SA.
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class PlayerException extends RuntimeException {

    private String playerId;
    private MooshakClassification classification;

    public PlayerException(String playerId, MooshakClassification classification) {
        this.playerId = playerId;
        this.classification = classification;
    }

    public PlayerException(String playerId, MooshakClassification classification, String message) {
        super(message);

        this.playerId = playerId;
        this.classification = classification;
    }

    public PlayerException(String playerId, MooshakClassification classification, String message, Throwable cause) {
        super(message, cause);

        this.playerId = playerId;
        this.classification = classification;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public MooshakClassification getClassification() {
        return classification;
    }

    public void setClassification(MooshakClassification classification) {
        this.classification = classification;
    }
}
