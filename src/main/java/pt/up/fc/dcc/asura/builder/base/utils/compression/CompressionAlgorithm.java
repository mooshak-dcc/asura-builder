package pt.up.fc.dcc.asura.builder.base.utils.compression;

/**
 * Common interface for compression algorithms
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public interface CompressionAlgorithm {

    String compress(String in, Object...args);
}
