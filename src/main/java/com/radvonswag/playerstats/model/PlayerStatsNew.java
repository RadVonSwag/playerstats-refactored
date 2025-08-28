package com.radvonswag.playerstats.model;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatsNew {
    private Map<String, Map<String, Integer>> playerStats = new HashMap<>();
    private int DataVersion;
    private String playerUUID;
    private String userName;

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getUUID() {
        return playerUUID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public Map<String, Map<String, Integer>> getPlayerStats() {
        return this.playerStats;
    }

    public Map<String, Integer> getUsedStats() {
        return playerStats.get("minecraft:used");
    }

    public Map<String, Integer> getCraftedStats() {
        return playerStats.get("minecraft:crafted");
    }

    public Map<String, Integer> getCustomStats() {
        return playerStats.get("minecraft:custom");
    }

    public Map<String, Integer> getKilledByStats() {
        return playerStats.get("minecraft:killed_by");
    }

    public Map<String, Integer> getBrokenStats() {
        return playerStats.get("minecraft:broken");
    }

    public Map<String, Integer> getKilledStats() {
        return playerStats.get("minecraft:killed");
    }

    public Map<String, Integer> getPickedUpStats() {
        return playerStats.get("minecraft:picked_up");
    }

    public Map<String, Integer> getMinedStats() {
        return playerStats.get("minecraft:mined");
    }

    public Map<String, Integer> getDroppedStats() {
        return playerStats.get("minecraft:dropped");
    }

    public int getDataVersion() {
        return this.DataVersion;
    }
}
