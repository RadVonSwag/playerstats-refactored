/**
 * This class obtains the Time Played stat in hours rounded to nearest whole
 * number for every player recorded to have joined the server.
 */
package com.radvonswag.playerstats.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radvonswag.playerstats.model.PlayerStatsNew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.radvonswag.playerstats.error.ErrorHandler.logErrorAndExit;

public class PlayTime {

    private static final int TIME_FACTOR = 72000;
    private static final int UUID_INDEX = 36;
    private final Logger log = LoggerFactory.getLogger(PlayTime.class);
    private String usercacheDir = "./usercache.json";
    private final ObjectMapper mapper = new ObjectMapper();
    Map<String, Integer> uuidAndTime = new HashMap<>();
    Map<String, Integer> usernameAndTime = new HashMap<>();

    /**
     * This asks the user what version their server is running since the way stats are handled changes
     * in future versions of the game.
     * It then proceeds with the stat retrieval and time calculation.
     * @param isPointTwelve is the string "y" or "n" depending on user's settings.
     * @param statsFiles an array of the files that contain the players' stats.
     * @param useWhitelist boolean to determine if the whitelist will be used as the usercache or not.
     */
    public Map<String, Integer> getTimePlayed(boolean isPointTwelve, File[] statsFiles, boolean useWhitelist) {
        if (useWhitelist) {
            usercacheDir = "./whitelist.json";
        }
        log.info("Gathering User Time Played");
        if (isPointTwelve) {
            handleLegacy(statsFiles);
        } else {
            handleNew(statsFiles);
        }
        return getUserNameAndTime();
    }

    private void handleNew(File[] statsFiles) {
        for (File currentFile : statsFiles) {
            try {
                JsonNode currentPlayer = mapper.readTree(currentFile);
                try {
                    String timePlayedAsString = currentPlayer.get("stats").get("minecraft:custom")
                        .get("minecraft:play_one_minute")
                        .toString();
                    int timePlayed = Integer.parseInt(timePlayedAsString) / TIME_FACTOR;
                    String uuid = currentFile.getName().substring(0, UUID_INDEX);
                    uuidAndTime.put(uuid, timePlayed);
                } catch (NullPointerException e) {
                    try {
                        String timePlayedAsString = currentPlayer.get("stats").get("minecraft:custom")
                            .get("minecraft:play_time")
                            .toString();
                        int timePlayed = Integer.parseInt(timePlayedAsString) / TIME_FACTOR;
                        String uuid = currentFile.getName().substring(0, UUID_INDEX);
                        uuidAndTime.put(uuid, timePlayed);
                    } catch (NullPointerException t) {
                        System.out.println("Error obtaining player stats. Please double check server version...");
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error obtaining player stats. Please double check server version...");
                System.exit(0);
            }
        }
    }

    private void handleLegacy(File[] statsFiles) {
        for (File currentFile : statsFiles) {
            try {
                JsonNode currentPlayer = mapper.readTree(currentFile);
                try {
                    String timePlayedAsString = currentPlayer.get("stat.playOneMinute")
                        .toString();
                    int timePlayed = Integer.parseInt(timePlayedAsString) / TIME_FACTOR;
                    String uuid = currentFile.getName().substring(0, UUID_INDEX);
                    uuidAndTime.put(uuid, timePlayed);
                } catch (NullPointerException e) {
                    System.out.println("Error obtaining player stats. Please double check server version...");
                    System.exit(0);
                }

            } catch (IOException e) {
                System.out.println("Error obtaining player stats. Please double check server version...");
                System.exit(0);
            }
        }
    }

    // TODO: replace this with associateUserName() function in PlayerDataHandler
    private Map<String, Integer> getUserNameAndTime() {
        JsonNode playerList;
        try {
            playerList = mapper.readTree(new File(usercacheDir));
            log.info("Players found: " + playerList.size());
            for (int i = 0; i < playerList.size(); i++) {
                String uuidKey = playerList.get(i).get("uuid").asText();
                int time = 0;
                if (uuidAndTime.get(uuidKey) != null) {
                    time = uuidAndTime.get(uuidKey);
                }
                usernameAndTime.put(playerList.get(i).get("name").asText(), time);
                //log.info("Found user: " + playerList.get(i).get("name").asText());
            }
        } catch(IOException e) {
            System.out.println("Error obtaining player stats. Please double check server version...");
            System.exit(0);
        }
        return usernameAndTime;
    }

    public Integer getPlayTimeNew(PlayerStatsNew playerStats) {
        Integer timePlayed = playerStats.getCustomStats().get("minecraft:play_one_minute");

        if (timePlayed == null) {
            timePlayed = playerStats.getCustomStats().get("minecraft:play_time");
        }

        if (timePlayed == null) {
            logErrorAndExit(log, "Error obtaining player stats. Please double check server version...");
        }
        return timePlayed / TIME_FACTOR;
    }

    // TODO: Figure out where to use this method
    public Map<String, Integer> getPlayTimeForAllPlayersNew(List<PlayerStatsNew> playerStatsList) {
        Map<String, Integer> uuidAndTimeMap = new HashMap<>();
        for (PlayerStatsNew currentPlayer : playerStatsList) {
            Integer playTime = getPlayTimeNew(currentPlayer);
            uuidAndTimeMap.put(currentPlayer.getUUID(), playTime);
        }
        return uuidAndTimeMap;
    }
}
