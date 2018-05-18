package pt.up.fc.dcc.asura.builder.languages;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Compile & execute programs in Javascript language
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class JavascriptLanguage extends Language {

    @Override
    public String compile(String gameSlug, String languageSlug, String programPath,
                          String basePath) {
        return basePath;
    }

    @Override
    public ProcessBuilder execute(String gameSlug, String languageSlug, String programPath, String playerId,
                                  String compileOutputPath) {

        Path playerWrapperPath = Paths.get(compileOutputPath, "wrappers", languageSlug, "PlayerWrapper.js");
        Path solutionPath = Paths.get(compileOutputPath).resolve(Paths.get(programPath).getFileName());

        List<String> tokens = new ArrayList<>();
        tokens.add("/usr/bin/js");
        tokens.add(playerWrapperPath.toString());
        tokens.add(solutionPath.toString());
        tokens.add(playerId);

        return new ProcessBuilder(tokens);
    }
}
