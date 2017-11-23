package org.roboy.memory;

import org.roboy.memory.ros.RosRun;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.net.URISyntaxException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.lang.ProcessBuilder;

public class Main
{
    private static Logger logger = Logger.getLogger(Main.class.toString());
    public static void main( String[] args ) throws URISyntaxException, IOException {

        Handler fh = new FileHandler("memory.log"); ///< File handler for a logger
        Logger.getLogger("").addHandler(fh);
        RosRun server = new RosRun(); ///< Initializing ROS server (ROS node)
        server.start();

        // Try Neo4j
        // TODO: Test and add Mac
        /*try {
            ProcessBuilder pb = new ProcessBuilder("ps -ef | grep Xvfb | grep -v grep", "~");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            InputStream is = p.getInputStream();
            if (is != null) {
                logger.info("Neo4j is running");
            } else {
                pb = new ProcessBuilder("neo4j start", "~");
                p = pb.start();
                is = p.getInputStream();
                logger.info(is.toString());
            }
            is.close();
        } catch (Exception err) {
            System.out.println(err);
        }*/
    }
}
