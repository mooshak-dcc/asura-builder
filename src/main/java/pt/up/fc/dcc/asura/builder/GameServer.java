package pt.up.fc.dcc.asura.builder;

import pt.up.fc.dcc.asura.builder.base.GameManager;
import pt.up.fc.dcc.asura.builder.base.GameState;
import pt.up.fc.dcc.asura.builder.base.exceptions.BuilderException;
import pt.up.fc.dcc.asura.builder.languages.JavaLanguage;
import pt.up.fc.dcc.asura.builder.languages.JavascriptLanguage;
import pt.up.fc.dcc.asura.builder.languages.Language;
import pt.up.fc.dcc.asura.builder.utils.FileUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Very simple HTTP server to run your game in a test scenario.
 * Feel free to adapt it to your game, since it is NOT used by Mooshak 2.0.
 *
 * @author José Paulo Leal <zp@dcc.fc.up.pt>
 * @author José Carlos Paiva <josepaiva94@gmail.com>
 */
public class GameServer {
    private static final int DEFAULT_PORT = 9999;

    private static final String[] IMAGE_EXTENSIONS = { "png", "svg", "jpg", "jpeg", "gif" };

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzzz");

    private final static String IMAGES_DIR = "images";

    private final static String INDEX_FILE = "index.html";
    private final static Map<String, String> MIMES = new HashMap<>();

    static {
        MIMES.put("html", "text/html");
        MIMES.put("js", "application/javascript");
        MIMES.put("png", "image/png");
        MIMES.put("svg", "image/svg+xml");
        MIMES.put("jpg", "image/jpeg");
        MIMES.put("jpeg", "image/jpeg");
        MIMES.put("gif", "image/gif");
    }

    private GameServer(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Started: accepting requests");
            while (true)
                try (Socket socket = serverSocket.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     OutputStream output = socket.getOutputStream()) {

                    String requestTarget = getRequest(input);
                    LinkedHashMap<String, String> fields = new LinkedHashMap<>();

                    fields.put("Date", DATE_FORMATTER.format(new Date()));
                    fields.put("Server", "Asura Builder Previewer");
                    fields.put("Vary", "accept-language,accept-charset");

                    if (!"/".equals(File.separator))
                        requestTarget = requestTarget.replace("/", File.separator);

                    String extension = getExtension(requestTarget);

                    if (Arrays.asList(IMAGE_EXTENSIONS).contains(extension))
                        requestTarget = File.separator + IMAGES_DIR + File.separator + requestTarget;

                    Path source = Paths.get(FileUtils.getPathResourcesFolder().toString(),
                            requestTarget).toAbsolutePath();

                    if (Files.isDirectory(source))
                        source = source.resolve(INDEX_FILE);

                    if (Files.isReadable(source)) {
                        fields.put("Content-Length", Files.getAttribute(source, "size").toString());
                        fields.put("Connection", "close");
                        fields.put("Content-Type", MIMES.get(extension));

                        sendResponseHeader(output, 200, "Ok", fields);
                        Files.copy(source, output);
                    } else {
                        fields.put("Connection", "close");
                        fields.put("Content-Type", "text/html; charset=utf-8");
                        sendResponseHeader(output, 404, "Not Found", fields);
                        output.write(("<h2>Not found:" + requestTarget + "</h2>\n").getBytes());
                    }
                }
        }
    }

    /**
     * Open an URL in an external browser, depending on the operating system
     *
     * @param url
     * @throws IOException
     */
    private static void openUrl(String url) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String operatingSystem = System.getProperty("os.name").toLowerCase();
        String browser;

        if (operatingSystem.contains("win"))
            browser = "rundll32 url.dll,FileProtocolHandler";
        else if (operatingSystem.contains("mac"))
            browser = "open";
        else if (operatingSystem.contains("nux"))
            browser = "xdg-open ";
        else
            throw new RuntimeException("Unrecognized operating system");

        runtime.exec(browser + " " + url);
    }

    /**
     * Instantiates a {@code GameSate} from a given binary name
     *
     * @param stateClassName class name that extends {@code GameSate}
     * @return instance of stateClassName
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static GameState getGameState(String stateClassName)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        Class<?> clazz = Class.forName(stateClassName);
        Object object = clazz.newInstance();

        if (object instanceof GameState)
            return (GameState) object;
        else
            throw new IllegalArgumentException("Not a game state class:" + stateClassName);
    }

    /**
     * Instantiates a {@code GameManager} from a given binary name
     *
     * @param managerClassName class name that extends {@code GameManager}
     * @return instance of managerClassName
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static GameManager getGameManager(String managerClassName)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        Class<?> clazz = Class.forName(managerClassName);
        Object object = clazz.newInstance();

        if (object instanceof GameManager)
            return (GameManager) object;
        else
            throw new IllegalArgumentException("Not a game manager class:" + managerClassName);
    }

    private String getRequest(BufferedReader input) throws IOException {
        String requestLine = input.readLine();
        System.out.println(requestLine);
        if (requestLine == null)
            return "/";
        String[] parts = requestLine.split(" ");
        return parts[1].split("\\?")[0];
    }

    private void sendResponseHeader(OutputStream output, int code, String phrase, LinkedHashMap<String, String> fields)
            throws IOException {
        output.write(("HTTP/1.0 " + code + " " + phrase + "\n").getBytes());
        for (String name : fields.keySet())
            output.write((name + ": " + fields.get(name) + "\n").getBytes());
        output.write("\n".getBytes());
    }

    /**
     * Get extension from file name
     *
     * @param name File name
     * @return extension
     */
    private String getExtension(String name) {
        int pos = name.lastIndexOf(".");
        if (pos == -1)
            return null;
        else
            return name.substring(pos + 1);
    }

    /**
     * Compile & execute a program of a player in a specific language
     *
     * @param gameSlug Slug of the game
     * @param languageName Language of the program
     * @param programPath Path to the program
     * @param playerId ID of the player
     * @return {@link ProcessBuilder} for creating processes of the program
     */
    private static ProcessBuilder compileAndExecute(String gameSlug, String languageName,
                                                    String programPath, String playerId) {

        Language language;
        switch (languageName) {

            case "java":
                language = new JavaLanguage();
                break;
            case "js":
            case "javascript":
            case "es6":
                language = new JavascriptLanguage();
                break;
            default:
                throw new BuilderException("Language not found!");
        }

        String codebasePath = language.prepare(gameSlug, languageName, programPath);
        String outputPath = language.compile(gameSlug, languageName, programPath, codebasePath);

        return language.execute(gameSlug, languageName, programPath, playerId, outputPath);
    }

    /**
     * Run the game and show movie on a browser
     *
     * @param args {@code String[]} Arguments of the program
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void main(String[] args)
            throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String home = System.getProperty("java.home");

        if (home == null)
            throw new RuntimeException("No java home");

        if (args.length < 2 || (args.length - 2) % 3 > 0) {
            System.err.println(
                    "GameServer: {gameSlug} {managerClassName} {language} {programPath} {playerId} "
                            + "... {language} {programPath} {playerId}");
            System.exit(0);
        }

        String gameSlug = args[0];
        String managerClassName = args[1];
        GameManager manager = getGameManager(managerClassName);

        if (args.length < (manager.getMinPlayersPerMatch() * 3) + 2) {
            System.err.println("GameServer: minimum players is " + manager.getMinPlayersPerMatch());
            System.exit(0);
        } else if (args.length > (manager.getMaxPlayersPerMatch() * 3) + 2) {
            System.err.println("GameServer: maximum players is " + manager.getMaxPlayersPerMatch());
            System.exit(0);
        }

        GameState gameState = getGameState(manager.getGameStateClassName());

        Map<String, Process> players = new HashMap<>();
        for (int i = 2; i < args.length; i += 3) {
            String language = args[i];
            String programPath = Paths.get(gameSlug, args[i + 1]).toString();
            String playerId = args[i + 2];

            ProcessBuilder builder = compileAndExecute(gameSlug, language, programPath, playerId);

            // builder.inheritIO();
            builder.redirectError(ProcessBuilder.Redirect.to(new File("errors.txt")));

            players.put(playerId, builder.start());
        }

        manager.manage(players);
        manager.exportGameMovie(Files.newOutputStream(
                FileUtils.getPathResourcesFolder().resolve("movie.json").toAbsolutePath()));

        openUrl("http://localhost:" + DEFAULT_PORT + "?playerId=" + players.keySet().iterator().next());
        new GameServer(DEFAULT_PORT);

        System.out.println("Running on localhost:" + DEFAULT_PORT);
    }

}
