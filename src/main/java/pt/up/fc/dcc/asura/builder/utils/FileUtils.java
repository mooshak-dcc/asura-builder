package pt.up.fc.dcc.asura.builder.utils;

import pt.up.fc.dcc.asura.builder.base.exceptions.BuilderException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Utilities used to manage and search file paths
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class FileUtils {

    /**
     * Get all paths that match a certain glob
     *
     * @param basePath Base path to start searching files
     * @param glob     Glob for matching files
     * @return {@link List} paths that match a certain glob
     */
    public static List<Path> getAllFilesMatchingGlob(String basePath, String glob) {

        List<Path> paths = new ArrayList<>();

        PathMatcher pathMatcher = FileSystems.getDefault()
                .getPathMatcher("glob:" +
                        (basePath + File.separator + glob)
                                .replaceAll(Pattern.quote(File.separator), "/"));

        try (Stream<Path> stream = Files.walk(Paths.get(basePath))) {
            stream.filter(pathMatcher::matches)
                    .forEach(paths::add);
        } catch (IOException e) {
            throw new BuilderException("Finding paths matching glob.");
        }

        return paths;
    }

    /**
     * Get the Java classpath separator according to the operating system
     *
     * @return the Java classpath separator according to the operating system
     */
    public static char getJavaClasspathSeparator() {

        String operatingSystem = System.getProperty("os.name").toLowerCase();

        if (operatingSystem.contains("win"))
            return ';';
        else
            return ':';
    }

    /**
     * Get path to resources folder
     *
     * @return {@link Path} path to resources folder
     */
    public static Path getPathResourcesFolder() {
        URL url = FileUtils.class.getClassLoader().getResource("");

        try {
            if (url == null)
                return Paths.get("");
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            return Paths.get("");
        }
    }

    /**
     * Copy a file or directory from a location to a target location
     *
     * @param source {@link File} source location
     * @param target {@link File} destination location
     * @throws IOException error copying file
     */
    public static void copy(File source, File target) throws IOException {
        if (source.isDirectory()) {
            copyDirectory(source, target);
        } else {
            copyFile(source, target);
        }
    }

    private static void copyDirectory(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdirs();
        }

        for (String f : Objects.requireNonNull(source.list())) {
            copy(new File(source, f), new File(target, f));
        }
    }

    private static void copyFile(File source, File target) throws IOException {

        try (
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target)
        ) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

    public static void copyResourceDirectory(final String sourceClasspath, final Path sourcePath,
                                             final Path rootTargetPath)
            throws IOException {

        URL url = FileUtils.class.getResource(sourceClasspath);
        File directory = new File(url.getFile());

        if (directory.exists()) {
            // we are running this code from filesystem

            Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    Path relativeFilePath = sourcePath.resolve(directory.toPath().relativize(file));

                    String sourceClasspath = "/" + relativeFilePath.toString()
                            .replaceAll(Pattern.quote(File.separator), "/");

                    copyResourceFile(sourceClasspath, relativeFilePath, rootTargetPath);

                    return FileVisitResult.CONTINUE;
                }
            });

        } else {
            // we are running this code from jar

            FileSystem fs;
            Path path;
            try {
                String pathToJar = url.toString().split("!")[0];
                fs = FileSystems.newFileSystem(URI.create(pathToJar), new HashMap<>());
                path = fs.getPath(sourcePath.toString());
            } catch (Exception e) {
                throw new IOException("Cannot get filesystem for JAR file!");
            }

            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {

                    String sourceClasspath = ("/" + file.toString()
                            .replaceAll(Pattern.quote(File.separator), "/"))
                            .replaceAll("//", "/");

                    Logger.getLogger("").info(sourceClasspath);

                    copyResourceFile(sourceClasspath,
                            rootTargetPath.resolve(file.toString().substring(1)).toFile());

                    return FileVisitResult.CONTINUE;
                }
            });

            fs.close();
        }

    }

    public static void copyResourceFile(String sourceClassPath, Path sourcePath, Path targetPath)
            throws IOException {

        Path targetFilePath = targetPath.resolve(sourcePath);

        copyResourceFile(sourceClassPath, targetFilePath.toFile());
    }

    public static void copyResourceFile(String sourceClassPath, File targetFile)
            throws IOException {

        if (!targetFile.exists())
            targetFile.getParentFile().mkdirs();

        Logger.getLogger("").info(sourceClassPath);

        try (
                InputStream in = FileUtils.class.getResourceAsStream(sourceClassPath);
                OutputStream out = new FileOutputStream(targetFile)
        ) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

    public static void copyResourceFile(String sourceClassPath, OutputStream targetOutputStream)
            throws IOException {

        try (
                InputStream in = FileUtils.class.getResourceAsStream(sourceClassPath);
        ) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                targetOutputStream.write(buf, 0, length);
            }
        }
    }
}
