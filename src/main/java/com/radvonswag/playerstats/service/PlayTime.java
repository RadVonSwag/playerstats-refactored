/**
 * This class obtains the Time Played stat in hours rounded to nearest whole
 * number for every player recorded to have joined the server.
 */
package com.radvonswag.playerstats.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PlayTime {

    private static final int TIME_FACTOR = 72000;
    private static final int UUID_INDEX = 36;
    private Logger log = Logger.getLogger(PlayTime.class.getName());
    private String usercacheDir = "./usercache.json";
    private final ObjectMapper mapper = new ObjectMapper();
    Map<String, Integer> uuidAndTime = new HashMap<String, Integer>();
    Map<String, Integer> usernameAndTime = new HashMap<String, Integer>();

    /**
     * This asks the user what version their server is running since the way stats are handled changes
     * in future versions of the game.
     * It then proceeds with the stat retrieval and time calculation.
     * @param isPointTwelve is the string "y" or "n" depending on user's settings.
     * @param statsFiles an array of the files that contain the players' stats.
     * @param useWhitelist boolean to determine if the whitelist will be used as the usercache or not.
     */
    public Map<String, Integer> getTimePlayed( String isPointTwelve, File[] statsFiles, boolean useWhitelist) {
        if (useWhitelist) {
            usercacheDir = "./whitelist.json";
        }
        log.info("Gathering User Time Played");
        if ("n".equalsIgnoreCase(isPointTwelve)) {
            handleNew(statsFiles);
        } else if ("y".equalsIgnoreCase(isPointTwelve)) {
            handleLegacy(statsFiles);
        }
        return getUserNameAndTime();
    }

    private void handleNew(File[] statsFiles) {
        for (int i = 0; i < statsFiles.length; i++) {
            File currentFile = statsFiles[i];
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
        for (int i = 0; i < statsFiles.length; i++) {
            File currentFile = statsFiles[i];
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
}
