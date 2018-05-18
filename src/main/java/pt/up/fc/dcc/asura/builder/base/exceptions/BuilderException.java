package pt.up.fc.dcc.asura.builder.base.exceptions;

/**
 * General exception thrown by the Asura Builder component
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class BuilderException extends RuntimeException {

    public BuilderException() {
    }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
