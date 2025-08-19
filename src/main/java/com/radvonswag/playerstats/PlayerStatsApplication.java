/**
 * This is the main class for the Playerstats program.
 */
package com.radvonswag.playerstats;

import com.radvonswag.playerstats.cache.UserCacheHandler;
import com.radvonswag.playerstats.playerdata.PlayerDataHandler;
import com.radvonswag.playerstats.service.PlayTime;
import com.radvonswag.playerstats.version.VersionHandler;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public final class PlayerStatsApplication {
    private static boolean doSaveVersion;
    private static boolean useWhitelist = false;

    public static void handleArgs(String[] args) {
        Options options = new Options();
        options.addOption("x", "dont-save", false, "Don't save version choice"); // This is primarily for testing in dev. Added as runtime arg in IDE
        options.addOption("w", "use-whitelist", false, "Use Whitelist instead of usercache");
        CommandLineParser parser= new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("x")) {
                System.out.println("Your version choice will not be saved.");
                doSaveVersion = false;
            }
            if(cmd.hasOption("w")) {
                System.out.println("Using whitelist.json instead of usercache.json");
                useWhitelist = true;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayStats(Map<String, Integer> stats) {
        System.out.println(String.format("%-39s", "").replace(' ', '_'));
        System.out.printf("%-26s %-12s\n", "Username", "Hours Played");
        System.out.println(String.format("%-39s", "").replace(' ', '_'));
        for (String key : stats.keySet()) {
            System.out.println(String.format("%-27s", key) + String.format("%-12s\n", stats.get(key)));
            System.out.println(String.format("%-39s", "").replace(' ', '_'));
        }
    }

    /**
     * Main function.
     */
    public static void main(String[] args) throws IOException {
        handleArgs(args);
        UserCacheHandler userCacheHandler = new UserCacheHandler();
        VersionHandler versionHandler = new VersionHandler();
        PlayerDataHandler playerDataHandler = new PlayerDataHandler();
        useWhitelist = userCacheHandler.userCacheCheck(useWhitelist);
        boolean isPointTwelve = versionHandler.checkServerVersion(doSaveVersion);
        File[] statsFiles = playerDataHandler.getPlayerDataDir();
        Map<String, Integer> stats = new PlayTime().getTimePlayed(isPointTwelve, statsFiles, useWhitelist);
        displayStats(stats);
    }
}
