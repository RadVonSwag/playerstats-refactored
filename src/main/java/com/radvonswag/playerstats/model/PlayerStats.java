package com.radvonswag.playerstats.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerStats {
    @JsonProperty("DataVersion")
    private int dataVersion;
    private Stats stats;
    private String playerUUID;
    private String userName;

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

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

    public void setDataVersion(int dataVersion) {
        this.dataVersion = dataVersion;
    }

    public int getDataVersion() {
        return this.dataVersion;
    }
}
