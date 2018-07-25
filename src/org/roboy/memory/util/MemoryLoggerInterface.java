package org.roboy.memory.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 * An Interface for Dialog or other programs to set the Memory Modules Logger Level
 *
 * Since we are using jave.util.levels for Logging in memory, we shall use the levels specified here:
 * https://docs.oracle.com/javase/8/docs/api/java/util/logging/Level.html
 */
public class MemoryLoggerInterface {
    private static Logger rootLogger = LogManager.getLogManager().getLogger("");

    /**
     * Sets level of Root and all Subhandlers to Level
     * @param level Level to Set to
     */
    public static void setLevel(Level level){
        rootLogger.setLevel(level);
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(level);
        }
    }

    /**
     * Sets level of Root and all Subhandlers to Level specified by a String
     * If str is invalid, we shall default to INFO setting
     * @param str
     */
    public static void setLogger(String str) {
        try {
            Level l =Level.parse(str.toUpperCase());
            setLevel(l);
        }catch (RuntimeException e){
            rootLogger.warning("Invalid Memory Logger Level Passed:\t" + str);
            rootLogger.warning("Please check Dialog's Config.Properties. Defaulting to INFO setting");
            setLevel(Level.INFO);
        }
    }
}
