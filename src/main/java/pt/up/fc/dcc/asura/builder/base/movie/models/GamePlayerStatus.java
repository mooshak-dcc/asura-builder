package pt.up.fc.dcc.asura.builder.base.movie.models;

import pt.up.fc.dcc.asura.builder.base.utils.JsonObject;

/**
 * Status of a player of the game
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class GamePlayerStatus extends JsonObject {
    private Integer points = 0;
    private MooshakClassification classification = null;
    private String observations = null;

    public GamePlayerStatus() {
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    /**
     * @return {@link MooshakClassification} the classification
     */
    public MooshakClassification getClassification() {
        return classification;
    }

    /**
     * @param classification {@link MooshakClassification}  the classification
     */
    public void setClassification(MooshakClassification classification) {
        this.classification = classification;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
