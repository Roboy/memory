package org.roboy.memory.util;


import java.util.logging.Logger;

public class LOGGER {

    private static java.util.logging.Logger LOGGER;
    private static LOGGER _instance;

    private LOGGER() {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    private Logger getLogger() {
        return LOGGER;
    }

    public Logger getInstance() {
        if(_instance == null) {
            _instance = new LOGGER();
        }

        return _instance.getLogger();
    }
}
