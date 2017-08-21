package org.roboy.memory.util;


/**
 * Configuration variables
 */
public class Config {
    //ROS
    public final static String ROS_MASTER_URI = "http://10.211.55.7:11311/";
    public final static String ROS_HOSTNAME = "10.211.55.2";  //IP ADDRESS OF CURRENT PC IN THE NETWORK

    //Neo4j
    //public final static String NEO4J_ADDRESS = "bolt://bot.roboy.org:7687";
    //public final static String NEO4J_USERNAME = "memory";
    //public final static String NEO4J_PASSWORD = "***********************"; //TODO: do NOT commit password to the repo

    //Local Neo4J
    public final static String NEO4J_ADDRESS = "bolt://127.0.0.1:7687";
    public final static String NEO4J_USERNAME = "neo4j";
    public final static String NEO4J_PASSWORD = "root";

}
