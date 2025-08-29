package com.radvonswag.playerstats.playerdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radvonswag.playerstats.cache.UserCacheHandler;
import com.radvonswag.playerstats.model.PlayerStatsLegacy;
import com.radvonswag.playerstats.model.PlayerStatsNew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.radvonswag.playerstats.error.ErrorHandler.logErrorAndExit;

public class PlayerDataHandler {

    private static String playerDataDir;
    private String userCacheDir = "./usercache.json";
    private static final int LEVEL_NAME_INDEX = 11;
    private final String serverPropertiesNotFoundErrorMessage = "Invalid server folder. The file \"server.properties\" was not found. "
            + "Please finish setting up the server and confirm that it runs, "
            + "has a world save, a usercache file, and a valid server.properties file.";
    private final String serverPropertiesInvalidErrorMessage = "Invalid server.properties file.";
    private final String statsFileNotFoundErrorMessage = "Stats files not found at: " + playerDataDir;
    private final Logger log = LoggerFactory.getLogger(PlayerDataHandler.class);
    private final UserCacheHandler userCacheHandler;

    public PlayerDataHandler(UserCacheHandler userCacheHandler) {
        this.userCacheHandler = userCacheHandler;
    }

    public PlayerStatsNew getPlayerDataNew(File statsFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String filename = statsFile.getName();
        String uuidString = filename.substring(0, filename.lastIndexOf('.'));
        PlayerStatsNew playerStats = objectMapper.readValue(statsFile, PlayerStatsNew.class);
        playerStats.setPlayerUUID(uuidString);
        playerStats.setUserName(associateUserName(playerStats));
        return playerStats;
    }

    // TODO: Implement Legacy Data Flow
    public PlayerStatsLegacy getPlayerDataLegacy(File statsFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PlayerStatsLegacy playerStats = objectMapper.readValue(statsFile, PlayerStatsLegacy.class);
        return playerStats;
    }

    //TODO: figure out how to tie this in or remove it.
    public List<PlayerStatsNew> getPlayerStatsListNew() throws IOException {
        List<PlayerStatsNew> playerStatsList = new ArrayList<>();
        File[] statsFiles = getPlayerDataDir();
        for (File currentFile : statsFiles) {
            PlayerStatsNew playerStats = getPlayerDataNew(currentFile);
            playerStatsList.add(playerStats);
        }
        return playerStatsList;
    }

    // TODO: Implement Legacy Data Flow
    public List<PlayerStatsLegacy> getPlayerStatsListLegacy() throws IOException {
        List<PlayerStatsLegacy> playerStatsList = new ArrayList<>();
        File[] statsFiles = getPlayerDataDir();
        for (File currentFile : statsFiles) {
            PlayerStatsLegacy playerStats = getPlayerDataLegacy(currentFile);
            playerStatsList.add(playerStats);
        }
        return playerStatsList;
    }

    public String associateUserName(PlayerStatsNew playerStats) {
        Map<String, String> userCache = userCacheHandler.loadUserCache();
        for (Map.Entry<String, String> entry : userCache.entrySet()) {
            if (playerStats.getUUID().equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "";
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
