package com.radvonswag.playerstats.error;

import org.slf4j.Logger;

public class ErrorHandler {

    public static void logError(Logger log, String errorMessage) {
        log.error(errorMessage);
    }

    public static void logErrorAndExit(Logger log, String errorMessage) {
        log.error(errorMessage);
        System.exit(1);
    }
}
