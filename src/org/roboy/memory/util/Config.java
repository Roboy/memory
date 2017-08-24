package org.roboy.memory.util;


/**
 * Configuration variables
 */
public class Config {
    //ROS
    public final static String ROS_MASTER_URI = "http://10.183.122.142:11311/";
    public final static String ROS_HOSTNAME = "10.183.63.119";  //IP ADDRESS OF CURRENT PC IN THE NETWORK
    
    //Neo4J
    public final static String NEO4J_ADDRESS = "bolt://10.183.122.142:7687";
    public final static String NEO4J_USERNAME = "neo4j";
    public final static String NEO4J_PASSWORD = "memory";

    //Redis
    public final static String REDIS_URI = "redis://bot.roboy.org:6379/0";
    public final static String REDIS_PASSWORD = "root"; //TODO: do NOT commit password to the repo
}
