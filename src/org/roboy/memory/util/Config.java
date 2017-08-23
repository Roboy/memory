package org.roboy.memory.util;


/**
 * Configuration variables
 */
public class Config {
    //ROS Lucas
    public final static String ROS_MASTER_URI = "http://10.211.55.7:11311/"; //192.168.224.128:63058/
    public final static String ROS_HOSTNAME = "10.211.55.2";  //IP ADDRESS OF CURRENT PC IN THE NETWORK

    //ROS Wagram
    //public final static String ROS_MASTER_URI = "http://10.183.55.157:11311/"; //192.168.224.128:63058/
    //public final static String ROS_HOSTNAME = "10.183.55.163";  //IP ADDRESS OF CURRENT PC IN THE NETWORK

    //Neo4j
    //public final static String NEO4J_ADDRESS = "bolt://bot.roboy.org:7687";
    //public final static String NEO4J_USERNAME = "*";
    //public final static String NEO4J_PASSWORD = "*"; //TODO: do NOT commit password to the repo
    //Local Neo4J
    public final static String NEO4J_ADDRESS = "bolt://127.0.0.1:7687";
    public final static String NEO4J_USERNAME = "neo4j";
    public final static String NEO4J_PASSWORD = "root";

    //Redis
    public final static String REDIS_URI = "redis://localhost:6379/0";
    public final static String REDIS_PASSWORD = "foobared";

    public static final String[] LABEL_VALUES = new String[] { "Person","Robot","Company","University","City","Country","Hobby","Occupation","Object" };
    public static final String[] RELATION_VALUES = new String[] { "FRIEND_OF","LIVE_IN","FROM","WORK_FOR","STUDY_AT","MEMBER_OF","HAS_HOBBY","KNOW","IS","PART_OF","IS_IN" };
}
