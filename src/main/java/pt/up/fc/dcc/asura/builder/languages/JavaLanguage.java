package pt.up.fc.dcc.asura.builder.languages;

import pt.up.fc.dcc.asura.builder.utils.FileUtils;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Compile & execute programs in Java language
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class JavaLanguage extends Language {

    @Override
    public String compile(String gameSlug, String languageSlug, String programPath,
                          String basePath) {

        String filename = Paths.get(programPath).getFileName().toString();

        List<Path> baseWrapperJavaFilesPaths = FileUtils.getAllFilesMatchingGlob(
                Paths.get(basePath, "wrappers", languageSlug).toString(),
                "*.java");

        List<Path> wrapperJavaFilesPaths = FileUtils.getAllFilesMatchingGlob(
                Paths.get(basePath, gameSlug, "wrappers", languageSlug).toString(),
                "*.java");

        List<String> compilerArgs = new ArrayList<>();
        compilerArgs.add("-cp");
        compilerArgs.add(getJarClasspath(gameSlug, languageSlug, basePath));
        compilerArgs.add("-d");
        compilerArgs.add(basePath);

        compilerArgs.addAll(baseWrapperJavaFilesPaths.stream().map(Path::toString).collect(Collectors.toSet()));
        compilerArgs.addAll(wrapperJavaFilesPaths.stream().map(Path::toString).collect(Collectors.toSet()));
        compilerArgs.add(Paths.get(basePath, filename).toString());

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, compilerArgs.toArray(new String[compilerArgs.size()]));

        return basePath;
    }

    @Override
    public ProcessBuilder execute(String gameSlug, String languageSlug, String programPath, String playerId,
                                  String compileOutputPath) {

        String home = System.getProperty("java.home");

        if (home == null)
            throw new RuntimeException("No java home");

        String classpath = getJarClasspath(gameSlug, languageSlug, compileOutputPath);

        if (!classpath.isEmpty())
            classpath += FileUtils.getJavaClasspathSeparator();

        classpath += compileOutputPath;

        String filename = Paths.get(programPath).getFileName().toString();
        String playerClassName = filename
                .substring(0, filename.length() - 5) // remove .java
                .replace(File.separatorChar, '.');

        return new ProcessBuilder(Paths.get(home, "bin", "java").toString(),
                "-cp", classpath, playerClassName, playerClassName, playerId);
    }

    /**
     * Get classpath string that contains JAR classes for {@code javac}
     *
     * @param gameSlug Slug of the game
     * @param language Language of the program
     * @param basePath Base path of game classes
     * @return classpath string that contains JAR classes
     */
    private String getJarClasspath(String gameSlug, String language, String basePath) {

        List<Path> baseWrapperJarFilesPaths = FileUtils.getAllFilesMatchingGlob(
                Paths.get(basePath, "wrappers", language).toString(),
                "*.jar");

        List<Path> wrapperJarFilesPaths = FileUtils.getAllFilesMatchingGlob(
                Paths.get(basePath, gameSlug, "wrappers", language).toString(),
                "*.jar");

        String classpathSeparator = FileUtils.getJavaClasspathSeparator() + "";
        String classpath =
                String.join(classpathSeparator,
                        String.join(classpathSeparator,
                                baseWrapperJarFilesPaths
                                        .stream()
                                        .map(Path::toString)
                                        .collect(Collectors.toSet())),
                        String.join(classpathSeparator,
                                wrapperJarFilesPaths
                                        .stream()
                                        .map(Path::toString)
                                        .collect(Collectors.toSet())));

        if (classpath.endsWith(classpathSeparator))
            classpath = classpath.substring(0, classpath.length() - 1);

        return classpath;
    }
}
