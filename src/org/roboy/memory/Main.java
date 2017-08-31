package org.roboy.memory;


import org.roboy.memory.ros.RosRun;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class Main
{
    public static void main( String[] args ) throws URISyntaxException, IOException {
        /** Logger configuration. Logs are stored in the file memory.log
         *
         */
        Handler fh = new FileHandler("memory.log");
        Logger.getLogger("").addHandler(fh);
        RosRun server = new RosRun();
        server.start();
    }
}
