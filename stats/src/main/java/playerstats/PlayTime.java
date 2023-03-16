package playerstats;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * This class obtains the Time Played stat in hours rounded to nearest whole
 * number for every player recorded to have joined the server.
 */
public class PlayTime {

    private static final int TIME_FACTOR = 72000;
    private static final int UUID_INDEX = 36;
    private Logger log = Logger.getLogger(PlayTime.class.getName());
    private String usercacheDir = "./usercache.json";
    private String statsDir;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * This asks the user what version their server is running since the way stats are handled changes
     * in future versions of the game.
     * It then proceeds with the stat retrieval and time calculation.
     * @param inStatsDir this is the stats directory retrieved in PlayerStats
     */
    public Map<String, Integer> getTimePlayed(String inStatsDir, boolean useWhitelist) {
        statsDir = inStatsDir;
        if (useWhitelist) {
            usercacheDir = "./whitelist.json";
        }
        log.info("user-stats directory: " + statsDir);
        Map<String, Integer> uuidAndTime = new HashMap<String, Integer>();
        Map<String, Integer> usernameAndTime = new HashMap<String, Integer>();
        Scanner input = new Scanner(System.in);

        System.out.println("Is this server 1.12.X? (Y/N)");
        String isPointTwelve = input.nextLine();
        while (true) {
            if (!"y".equalsIgnoreCase(isPointTwelve) && !"n".equalsIgnoreCase(isPointTwelve)) {
                System.out.println("(Y/N)\n");
                isPointTwelve = input.nextLine();
                continue;
            } else {
                break;
            }
        }
        input.close();
        File[] statsFiles = new File(statsDir).listFiles();

        if ("n".equalsIgnoreCase(isPointTwelve)) {
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
                        System.out.println("Error obtaining player stats. Please double check server version...");
                        System.exit(0);
                    }
                } catch (IOException e) {
                    System.out.println("Error obtaining player stats. Please double check server version...");
                    System.exit(0);
                }
            }
        } else if ("y".equalsIgnoreCase(isPointTwelve)) {
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

        JsonNode playerList;

        try {
            playerList = mapper.readTree(new File(usercacheDir));
            for (int i = 0; i < playerList.size(); i++) {
                String uuidKey = playerList.get(i).get("uuid").asText();
                int time = 0;
                if (uuidAndTime.get(uuidKey) != null) {
                    time = uuidAndTime.get(uuidKey);
                }
                usernameAndTime.put(playerList.get(i).get("name").asText(), time);
                //log.info("Found user: " + playerList.get(i).get("name").asText());
            }
        } catch (IOException e) {
            System.out.println("Error: Could not find usercache...");
            System.exit(0);
        }
        return usernameAndTime;
    }
}
