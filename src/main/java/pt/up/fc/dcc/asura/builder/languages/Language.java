package pt.up.fc.dcc.asura.builder.languages;

import pt.up.fc.dcc.asura.builder.base.exceptions.BuilderException;
import pt.up.fc.dcc.asura.builder.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Abstract class extended by languages that defines three methods:
 * {@code prepare(gameSlug,language,programPath)},
 * {@code compile(gameSlug,language,programPath,basePath)},
 * and {@code execute(gameSlug,compileOutputPath,programPath)}
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public abstract class Language {

    /**
     * Prepare to compile player program
     *
     * @param gameSlug     Short name of the game
     * @param languageSlug Short name of the language in which player is coded
     * @param programPath  path of the program that extends {@code Player}
     * @return base output path of preparation
     */
    public String prepare(String gameSlug, String languageSlug, String programPath) {

        Path outputPath;
        try {
            outputPath = Files.createTempDirectory("asura-builder-" +
                    Long.toString(System.nanoTime()));
            outputPath.toFile().deleteOnExit();
        } catch (IOException e) {
            throw new BuilderException("Creating temporary directory.");
        }

        // copy base wrapper files to output folder
        Path baseWrapperRelativePath = Paths.get("wrappers", languageSlug);

        try {
            FileUtils.copyResourceDirectory(baseWrapperRelativePath, outputPath);
        } catch (IOException e) {
            throw new BuilderException("Error copying base wrapper files to the output folder.");
        }

        // copy wrapper files to output folder
        Path wrapperRelativePath = Paths.get(gameSlug, "wrappers", languageSlug);

        try {
            FileUtils.copyResourceDirectory(wrapperRelativePath, outputPath);
        } catch (IOException e) {
            throw new BuilderException("Error copying wrapper files to the output folder.");
        }

        // copy solution to the output folder
        try {
            FileUtils.copyResourceFile(Paths.get(programPath),
                    outputPath.resolve(Paths.get(programPath).getFileName()).toFile());
        } catch (IOException e) {
            throw new BuilderException("Error copying solution to the output folder.");
        }

        return outputPath.toString();
    }

    /**
     * Compile player program in a specific language
     *
     * @param gameSlug     Short name of the game
     * @param languageSlug Short name of the language in which player is coded
     * @param programPath  Path of the program that extends {@code Player}
     * @param basePath     Base path of the program code after preparation
     * @return output path
     */
    public abstract String compile(String gameSlug, String languageSlug, String programPath,
                                   String basePath);

    /**
     * Create a {@link ProcessBuilder} for a player in a specific language
     *
     * @param gameSlug          Short name of the game
     * @param languageSlug      Short name of the language in which player is coded
     * @param programPath       Path of the program that extends {@code Player}
     * @param playerId          ID of the player
     * @param compileOutputPath Output path of the compilation
     * @return {@link ProcessBuilder} for creating processes of the program
     */
    public abstract ProcessBuilder execute(String gameSlug, String languageSlug, String programPath,
                                           String playerId, String compileOutputPath);

}
