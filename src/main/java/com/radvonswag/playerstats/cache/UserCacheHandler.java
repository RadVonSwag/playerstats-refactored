package com.radvonswag.playerstats.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radvonswag.playerstats.model.UserCacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.radvonswag.playerstats.error.ErrorHandler.logErrorAndExit;
import static com.radvonswag.playerstats.error.ErrorHandler.logError;

public class UserCacheHandler {
    private static final int USERCACHE_BUFFER = 39;
    private final String userCacheFileName = "usercache.json";
    private final String userCacheNotFoundErrorMessage = "Invalid server folder. The file \"usercache.json\" was not found. "
            + "Please finish setting up the server and confirm that it runs, "
            + "has a world save, a usercache file, and a valid server.properties file."
            + "Alternatively, if you have a whitelist file, try running with \"-w\" as an argument.";
    private final String userCacheFormatErrorMessage = "The usercache.json file is formatted incorrectly. Checking server whitelist as backup...";
    private final String whiteListNotFoundErrorMessage = "Could not find whitelist.json. Unable to parse UUIDs and Usernames.\nExiting...";
    private final String whiteListWarningMessage = "Using whitelist.json file as backup. The dataset will not contain users "
            + "that have joined your server before the whitelist was implemented "
            + "and are not currently on the whitelist.";

    private static final Logger log = LoggerFactory.getLogger(UserCacheHandler.class);

    public UserCacheHandler() {}

    /**
     * This method checks for the usercache.json file and if it doesn't exist it
     * will attempt to use the whitelist.json
     * as backup and if that does not exist, the program will log an error and exit.
     */
    public boolean userCacheCheck(boolean useWhitelist) {
        if (!useWhitelist) {
            File userCache = new File(userCacheFileName);
            if (!userCache.exists()) {
                logErrorAndExit(log, userCacheNotFoundErrorMessage);
            }
            if (userCache.length() < USERCACHE_BUFFER) {
                logError(log, userCacheFormatErrorMessage);
            }
            return false;
        }

        String whiteListFileName = "whitelist.json";
        File whiteList = new File(whiteListFileName);
        if (!whiteList.exists()) {
            logErrorAndExit(log, whiteListNotFoundErrorMessage);
        }
        log.info(whiteListWarningMessage);
        return true;
    }

    public Map<String, String> loadUserCache() {
        Map<String, String> userCache = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<UserCacheEntry> tempList = objectMapper.readValue(
                    new File(userCacheFileName),
                    new TypeReference<List<UserCacheEntry>>() {}
            );

            for (UserCacheEntry entry : tempList) {
                userCache.put(entry.getUUID(), entry.getName());
            }

        } catch (Exception e) {
            logErrorAndExit(log, e.getMessage());
        }
        return userCache;
    }
}
