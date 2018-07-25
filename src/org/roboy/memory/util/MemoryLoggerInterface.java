package org.roboy.memory.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MemoryLoggerInterface {
    private static Logger rootLogger = LogManager.getLogManager().getLogger("");

    public static void setLevel(Level level){
        rootLogger.setLevel(level);
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(level);
        }
    }
    public static void setLogger(String i) {
        try {
            Level l =Level.parse(i.toUpperCase());
            setLevel(l);
        }catch (RuntimeException e){
            rootLogger.warning("Invalid Memory Logger Level Passed:\t" + i);
            rootLogger.warning("Please check Dialog's Config.Properties. Defaulting to INFO setting");
            setLevel(Level.INFO);
        }
    }
}
