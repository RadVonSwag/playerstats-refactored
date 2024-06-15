package playerstats;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

/**
 * This is the main class for the Playerstats program.
 */
public final class PlayerStats {
    private static final int LEVEL_NAME_INDEX = 11;
    private static final int USERCACHE_BUFFER = 39;
    private static boolean useWhitelist;

    private PlayerStats() {
    }

    /**
     * This method dynamically retrieves the world save filename from the server
     * folder's server.properties
     * file for use in obtaining playerstats.
     * @throws FileNotFoundException
     */
    public static String getPlayerDataDir() throws FileNotFoundException {
        File serverProperties = new File("server.properties");
        System.out.println(System.getProperty("user.dir"));
        String playerDataDir = null;
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
            Scanner reader = new Scanner(serverProperties, StandardCharsets.UTF_8.name());
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.contains("level-name")) {
                    playerDataDir = line.substring(LEVEL_NAME_INDEX);
                    break;
                }
            }
            reader.close();
            playerDataDir = playerDataDir + "/stats";
        }
        return playerDataDir;
    }

    /**
     * This method checks for the usercache.json file and if it doesn't exist it
     * will attempt to use the whitelist.json
     * as backup and if that does not exist, the program will exit.
     * @return
     */
    public static boolean userCacheCheck() {
        File userCache = new File("usercache.json");
        if (!userCache.exists()) {
            System.out.println(
                    "Invalid server folder. The file \"usercache.json\" was not found. "
                            + "Please finish setting up the server and confirm that it runs, "
                            + "has a world save, a usercache file, and a valid server.properties file.");
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
                                + "and are not on the whitelist.");
                useWhitelist = true;
            }
        }

        return useWhitelist;
    }

    /**
     * Main function.
     */
    public static void main(String[] args) throws FileNotFoundException {
        PlayTime playTime = new PlayTime();
        String statsDir = getPlayerDataDir();
        Map<String, Integer> stats = playTime.getTimePlayed(statsDir, userCacheCheck());
        System.out.println(String.format("%-39s", "").replace(' ', '_'));
        System.out.printf("%-26s %-12s\n", "Username", "Hours Played");
        System.out.println(String.format("%-39s", "").replace(' ', '_'));
        for (String key : stats.keySet()) {
            System.out.println(String.format("%-27s", key) + String.format("%-12s\n", stats.get(key)));
            System.out.println(String.format("%-39s", "").replace(' ', '_'));
        }
    }
}
