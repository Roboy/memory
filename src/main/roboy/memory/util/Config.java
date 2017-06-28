package memory.util;


/**
 * Configuration variables
 */
public class Config {
    //ROS
    public final static String ROS_MASTER_URI = "http://172.16.247.130:11311/";
    public final static String ROS_HOSTNAME = "172.16.247.1";  //IP ADDRESS OF CURRENT PC IN THE NETWORK
    //Neo4j
    public final static String NEO4J_ADDRESS = "bolt://bot.roboy.org:7687";
    public final static String NEO4J_USERNAME = "memory";
    public final static String NEO4J_PASSWORD = "************************"; //TODO: do NOT commit password to the repo

}
