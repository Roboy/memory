package org.roboy.memory.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerInterface {
    private static Logger rootLogger = LogManager.getLogManager().getLogger("");

    public static void setLevel(Level level){
        rootLogger.setLevel(level);
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(level);
        }
    }
    public static void setLogger(int i){
        if(i==0) {setLevel(Level.OFF);}
        else if(i==1) {setLevel(Level.WARNING);}
        else if(i==2) {setLevel(Level.INFO);}
        else rootLogger.warning("Invalid Integer Passed:\t"+i);
    }
}
