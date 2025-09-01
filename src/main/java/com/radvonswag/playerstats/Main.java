package com.radvonswag.playerstats;

import com.radvonswag.playerstats.cache.UserCacheHandler;
import com.radvonswag.playerstats.playerdata.PlayerDataHandler;
import com.radvonswag.playerstats.version.VersionHandler;

import java.io.IOException;

public class Main {
    /**
     * Main function.
     */
    public static void main(String[] args) throws IOException {
        UserCacheHandler userCacheHandler = new UserCacheHandler();
        VersionHandler versionHandler = new VersionHandler();
        PlayerDataHandler playerDataHandler = new PlayerDataHandler(userCacheHandler);

        PlayerStatsApplication playerStatsApplication = new PlayerStatsApplication(userCacheHandler, versionHandler, playerDataHandler);

        playerStatsApplication.run(args);
    }
}
