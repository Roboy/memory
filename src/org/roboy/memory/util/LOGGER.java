package org.roboy.memory.util;


import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class LOGGER {
    public static void setUp() throws IOException {
        //logger configuration
        Handler fh = new FileHandler("/memory.log");
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).addHandler(fh);
    }
}
