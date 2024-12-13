/**
 * Placeholder javadoc
 */
package com.radvonswag.playerstats.model;

public class Player {
    //Simple playerstats until I can figure out how to efficiently get all of them.
    int playOneMinute;
    int fishCaught;
    int deaths;
    int playerKills;
    int jump;
    int leaveGame;
    int sprintOneCm;

    public Player() {
    }
    public Player(int playOneMinute, int fishCaught, int deaths, int playerKills, int jump, int leaveGame,
            int sprintOneCm) {
        this.playOneMinute = playOneMinute;
        this.fishCaught = fishCaught;
        this.deaths = deaths;
        this.playerKills = playerKills;
        this.jump = jump;
        this.leaveGame = leaveGame;
        this.sprintOneCm = sprintOneCm;
    }
    public int getplayOneMinute() {
        return playOneMinute;
    }
    public void setplayOneMinute(int playOneMinute) {
        this.playOneMinute = playOneMinute;
    }
    public int getfishCaught() {
        return fishCaught;
    }
    public void setfishCaught(int fishCaught) {
        this.fishCaught = fishCaught;
    }
    public int getDeaths() {
        return deaths;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public int getPlayer_kills() {
        return playerKills;
    }
    public void setPlayer_kills(int playerKills) {
        this.playerKills = playerKills;
    }
    public int getJump() {
        return jump;
    }
    public void setJump(int jump) {
        this.jump = jump;
    }
    public int getLeave_game() {
        return leaveGame;
    }
    public void setLeave_game(int leaveGame) {
        this.leaveGame = leaveGame;
    }
    public int getSprint_one_cm() {
        return sprintOneCm;
    }
    public void setSprint_one_cm(int sprintOneCm) {
        this.sprintOneCm = sprintOneCm;
    }
}
