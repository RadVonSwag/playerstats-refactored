/**
 * This is the main class for the Playerstats program.
 */
package com.radvonswag.playerstats;

import com.radvonswag.playerstats.cache.UserCacheHandler;
import com.radvonswag.playerstats.model.PlayerStats;
import com.radvonswag.playerstats.playerdata.PlayerDataHandler;
import com.radvonswag.playerstats.service.PlayTime;
import com.radvonswag.playerstats.version.VersionHandler;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.radvonswag.playerstats.error.ErrorHandler.logErrorAndExit;

public final class PlayerStatsApplication {
    private static final Logger log = LoggerFactory.getLogger(PlayerStatsApplication.class);
    private static boolean doSaveVersion;
    private static boolean useWhitelist = false;

    private final UserCacheHandler userCacheHandler;
    private final VersionHandler versionHandler;
    private final PlayerDataHandler playerDataHandler;

    public PlayerStatsApplication(UserCacheHandler userCacheHandler, VersionHandler versionHandler, PlayerDataHandler playerDataHandler) {
        this.userCacheHandler = userCacheHandler;
        this.versionHandler = versionHandler;
        this. playerDataHandler = playerDataHandler;
    }

    public static void handleArgs(String[] args) {
        Options options = new Options();
        options.addOption("x", "dont-save", false, "Don't save version choice"); // This is primarily for testing in dev. Added as runtime arg in IDE
        options.addOption("w", "use-whitelist", false, "Use Whitelist instead of usercache");
        CommandLineParser parser= new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("x")) {
                log.info("Your version choice will not be saved.");
                doSaveVersion = false;
            }
            if(cmd.hasOption("w")) {
                log.info("Using whitelist.json instead of usercache.json");
                useWhitelist = true;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayTimePlayed(Map<String, Integer> stats) {
        System.out.println(String.format("%-39s", "").replace(' ', '_'));
        System.out.printf("%-26s %-12s\n", "Username", "Hours Played");
        System.out.println(String.format("%-39s", "").replace(' ', '_'));
        for (String key : stats.keySet()) {
            System.out.println(String.format("%-27s", key) + String.format("%-12s\n", stats.get(key)));
            System.out.println(String.format("%-39s", "").replace(' ', '_'));
        }
    }

    public void run(String[] args) throws IOException {
        handleArgs(args);
        log.info("Starting Player Stats Application in {}", System.getProperty("user.dir"));
        useWhitelist = userCacheHandler.userCacheCheck(useWhitelist);
        boolean isLegacy = versionHandler.checkServerVersion(doSaveVersion);

        if (isLegacy) {
            // TODO: Implement Legacy Flow
            logErrorAndExit(log, "So Sorry! Player Stats currently unavailable for versions before 1.13 :(");
        }

        List<PlayerStats> listOfPlayerStats = playerDataHandler.getPlayerStatsList();
        log.info("Player Stats successfully retrieved for {} players", listOfPlayerStats.size());

        Map<String, Integer> playTimeStats = new PlayTime().getPlayTimeForAllPlayers(listOfPlayerStats);
        displayTimePlayed(playTimeStats);
    }
}
