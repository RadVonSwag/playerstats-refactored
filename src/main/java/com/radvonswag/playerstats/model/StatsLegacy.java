package com.radvonswag.playerstats.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

// TODO: Implement Legacy Stats Flow. This whole class is not formatted for legacy stats (pre 1.12)
public class StatsLegacy {

    @JsonProperty("minecraft:used")
    private Map<String, Integer> used;

    @JsonProperty("minecraft:crafted")
    private Map<String, Integer> crafted;

    @JsonProperty("minecraft:custom")
    private Map<String, Integer> custom;

    @JsonProperty("minecraft:killed_by")
    private Map<String, Integer> killedBy;

    @JsonProperty("minecraft:broken")
    private Map<String, Integer> broken;

    @JsonProperty("minecraft:killed")
    private Map<String, Integer> killed;

    @JsonProperty("minecraft:picked_up")
    private Map<String, Integer> pickedUp;

    @JsonProperty("minecraft:mined")
    private Map<String, Integer> mined;

    @JsonProperty("minecraft:dropped")
    private Map<String, Integer> dropped;

    // Getters

    public Map<String, Integer> getUsed() {
        return used;
    }

    public Map<String, Integer> getCrafted() {
        return crafted;
    }

    public Map<String, Integer> getCustom() {
        return custom;
    }

    public Map<String, Integer> getKilledBy() {
        return killedBy;
    }

    public Map<String, Integer> getBroken() {
        return broken;
    }

    public Map<String, Integer> getKilled() {
        return killed;
    }

    public Map<String, Integer> getPickedUp() {
        return pickedUp;
    }

    public Map<String, Integer> getMined() {
        return mined;
    }

    public Map<String, Integer> getDropped() {
        return dropped;
    }

    // Setters

    public void setUsed(Map<String, Integer> used) {
        this.used = used;
    }

    public void setCrafted(Map<String, Integer> crafted) {
        this.crafted = crafted;
    }

    public void setCustom(Map<String, Integer> custom) {
        this.custom = custom;
    }

    public void setKilledBy(Map<String, Integer> killedBy) {
        this.killedBy = killedBy;
    }

    public void setBroken(Map<String, Integer> broken) {
        this.broken = broken;
    }

    public void setKilled(Map<String, Integer> killed) {
        this.killed = killed;
    }

    public void setPickedUp(Map<String, Integer> pickedUp) {
        this.pickedUp = pickedUp;
    }

    public void setMined(Map<String, Integer> mined) {
        this.mined = mined;
    }

    public void setDropped(Map<String, Integer> dropped) {
        this.dropped = dropped;
    }
}
