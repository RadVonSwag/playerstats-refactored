/**
 * This class obtains the Time Played stat in hours rounded to nearest whole
 * number for every player recorded to have joined the server.
 */
package com.radvonswag.playerstats.service;

import com.radvonswag.playerstats.model.PlayerStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.radvonswag.playerstats.error.ErrorHandler.logErrorAndExit;

public class PlayTime {

    private static final int TIME_FACTOR = 72000;
    private final Logger log = LoggerFactory.getLogger(PlayTime.class);

    public Integer getPlayTime(PlayerStats playerStats) {
        Integer timePlayed = playerStats
                .getStats()
                .getCustom()
                .get("minecraft:play_one_minute");

        if (timePlayed == null) {
            timePlayed = playerStats
                    .getStats()
                    .getCustom()
                    .get("minecraft:play_time");
        }

        if (timePlayed == null) {
            logErrorAndExit(log, "Error obtaining player stats. Please double check server version...");
        }
        return timePlayed / TIME_FACTOR;
    }

    public Map<String, Integer> getPlayTimeForAllPlayers(List<PlayerStats> playerStatsList) {
        Map<String, Integer> userNameAndTimeMap = new HashMap<>();
        for (PlayerStats currentPlayer : playerStatsList) {
            Integer playTime = getPlayTime(currentPlayer);
            userNameAndTimeMap.put(currentPlayer.getUserName(), playTime);
        }
        return userNameAndTimeMap;
    }
}
