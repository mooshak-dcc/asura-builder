package pt.up.fc.dcc.asura.builder.base.movie.models;

/**
 * Classifications accepted by Mooshak
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public enum MooshakClassification {
    ACCEPTED,
    PRESENTATION_ERROR,
    WRONG_ANSWER,
    EVALUATION_SKIPPED,
    OUTPUT_LIMIT_EXCEEDED,
    MEMORY_LIMIT_EXCEEDED,
    TIME_LIMIT_EXCEEDED,
    INVALID_FUNCTION,
    INVALID_EXIT_VALUE,
    RUNTIME_ERROR,
    REQUIRES_REEVALUATION;

    public String toString() {
        return super.toString();
    }
}
