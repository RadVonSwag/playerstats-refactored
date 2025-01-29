/**
 * This is the main class for the Playerstats program.
 */
package com.radvonswag.playerstats;

import com.radvonswag.playerstats.service.PlayTime;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public final class PlayerStatsApplication {
    private static final int LEVEL_NAME_INDEX = 11;
    private static final int USERCACHE_BUFFER = 39;
    private static boolean doSaveVersion;
    private static boolean useWhitelist;
    private static String isPointTwelve;
    private static String playerDataDir;
    private static final String VERSION_CONFIG_DIR = "./playerstats/config/minecraft_version.dat";

    public static void handleArgs(String[] args) {
        Options options = new Options();
        options.addOption("x", "dont-save", false, "Don't save version choice"); // This is primarily for testing in dev. Added as runtime arg in IDE
        options.addOption("w", "use-whitelist", false, "Use Whitelist instead of usercache");
        CommandLineParser parser= new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("x")) {
                System.out.println("Your version choice will not be saved.");
                doSaveVersion = false;
            }
            if(cmd.hasOption("w")) {
                System.out.println("Using whitelist.json instead of usercache.json");
                useWhitelist = true;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method checks to see if the user has specified the version once before, making it so that they do not have to enter the version every time.
     * @return "y" or "n" based on the content of the version file or the users' input if first time running.
     *
     */
    public static String versionCheck() throws IOException {
        File minecraftVersionFile = new File(VERSION_CONFIG_DIR);
        if (!minecraftVersionFile.exists() || !doSaveVersion) {
            Scanner input = new Scanner(System.in);
            System.out.println("Is this server on 1.12 (or older)? (Y/N)");
            isPointTwelve = input.nextLine();
            while (true) {
                if (!"y".equalsIgnoreCase(isPointTwelve) && !"n".equalsIgnoreCase(isPointTwelve)) {
                    System.out.println("(Y/N)\n");
                    isPointTwelve = input.nextLine();
                } else {
                    break;
                }
            }
            if (doSaveVersion) {
                saveVersionToFile(minecraftVersionFile);
            }
            input.close();
        } else {
            Scanner reader = new Scanner(minecraftVersionFile);
            isPointTwelve = reader.nextLine();
            reader.close();
        }
        return isPointTwelve;
    }

    public static void saveVersionToFile(File minecraftVersionFile) throws IOException {
        if (minecraftVersionFile.getParentFile().mkdirs()) {
            FileWriter writer = new FileWriter(VERSION_CONFIG_DIR);
            writer.write(isPointTwelve);
            writer.close();
        }
    }

    /**
     * This method dynamically retrieves the world save filename from the server
     * folder's server.properties
     * file for use in obtaining playerstats.
     */
    public static File[] getPlayerDataDir() throws FileNotFoundException {
        File serverProperties = new File("server.properties");
        System.out.println(System.getProperty("user.dir"));
        if (!serverProperties.exists()) {
            System.out.println(
                    "Invalid server folder. The file \"server.properties\" was not found. "
                            + "Please finish setting up the server and confirm that it runs, "
                            + "has a world save, a usercache file, and a valid server.properties file.");
            System.out.println("Exiting...");
            System.exit(0);
        } else if (serverProperties.length() == 0) {
            System.out.println("Invalid server.properties file.");
            System.out.println("Exiting...");
            System.exit(0);
        } else {
            playerDataDir = findDataDir(serverProperties);
        }
        File[] statsFiles = new File(playerDataDir).listFiles();
        if (statsFiles == null) {
            System.out.println("Stats files not found at: " + playerDataDir);
            System.exit(0);
        }
        return statsFiles;
    }

    /**
     * findDataDir is a helper method for getPlayerDataDir
     */
    private static String findDataDir(File serverProperties) throws FileNotFoundException {
        Scanner reader = new Scanner(serverProperties, StandardCharsets.UTF_8.name());
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.contains("level-name")) {
                playerDataDir = line.substring(LEVEL_NAME_INDEX);
                break;
            }
        }
        reader.close();
        return playerDataDir + "/stats";
    }

    /**
     * This method checks for the usercache.json file and if it doesn't exist it
     * will attempt to use the whitelist.json
     * as backup and if that does not exist, the program will exit.
     */
    public static boolean userCacheCheck() {
        File userCache = new File("usercache.json");
        if (!userCache.exists()) {
            System.out.println(
                    "Invalid server folder. The file \"usercache.json\" was not found. "
                            + "Please finish setting up the server and confirm that it runs, "
                            + "has a world save, a usercache file, and a valid server.properties file."
                            + "Alternatively, if you have a whitelist file, try running with \"-w\" as an argument.");
        } else if (userCache.length() < USERCACHE_BUFFER) {
            System.out.println(
                    "The usercache.json file is formatted incorrectly. Checking server whitelist as backup...");
            File whiteList = new File("whitelist.json");
            if (!whiteList.exists()) {
                System.out.println("Could not find whitelist.json. Unable to parse UUIDs and Usernames.\nExiting...");
                System.exit(0);
            } else {
                System.out.println(
                        "Using whitelist.json file as backup. The dataset will not contain users "
                                + "that have joined your server before the whitelist was implemented "
                                + "and are not currently on the whitelist.");
                useWhitelist = true;
            }
        }

        return useWhitelist;
    }

    /**
     * Main function.
     */
    public static void main(String[] args) throws IOException {
        handleArgs(args);
        Map<String, Integer> stats = new PlayTime().getTimePlayed(versionCheck(), getPlayerDataDir(), userCacheCheck());
        System.out.println(String.format("%-39s", "").replace(' ', '_'));
        System.out.printf("%-26s %-12s\n", "Username", "Hours Played");
        System.out.println(String.format("%-39s", "").replace(' ', '_'));
        for (String key : stats.keySet()) {
            System.out.println(String.format("%-27s", key) + String.format("%-12s\n", stats.get(key)));
            System.out.println(String.format("%-39s", "").replace(' ', '_'));
        }
    }
}
