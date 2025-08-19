package com.radvonswag.playerstats.playerdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static com.radvonswag.playerstats.error.ErrorHandler.logErrorAndExit;

public class PlayerDataHandler {

    private static String playerDataDir;
    private static final int LEVEL_NAME_INDEX = 11;
    private final String serverPropertiesNotFoundErrorMessage = "Invalid server folder. The file \"server.properties\" was not found. "
            + "Please finish setting up the server and confirm that it runs, "
            + "has a world save, a usercache file, and a valid server.properties file.";
    private final String serverPropertiesInvalidErrorMessage = "Invalid server.properties file.";
    private final String statsFileNotFoundErrorMessage = "Stats files not found at: " + playerDataDir;
    private final Logger log = LoggerFactory.getLogger(PlayerDataHandler.class);

    public PlayerDataHandler() {

    }

    /**
     * This method dynamically retrieves the world save filename from the server
     * folder's server.properties
     * file for use in obtaining playerstats.
     */
    public File[] getPlayerDataDir() throws FileNotFoundException {
        File serverProperties = new File("server.properties");
        log.info("Current Server Directory: " + System.getProperty("user.dir"));
        if (!serverProperties.exists()) {
            logErrorAndExit(log, serverPropertiesNotFoundErrorMessage);
        } else if (serverProperties.length() == 0) {
            logErrorAndExit(log, serverPropertiesInvalidErrorMessage);
        } else {
            playerDataDir = findDataDir(serverProperties);
        }
        File[] statsFiles = new File(playerDataDir).listFiles();
        if (statsFiles == null) {
            logErrorAndExit(log, statsFileNotFoundErrorMessage);
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
}
