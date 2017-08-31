package org.roboy.memory.util;


/**
 * Configuration variables.
 */
public class Config {

    /**
     * Configuration of the Connection to ROS, Neo4J and Redis Server
     */
    ///ROS
    //public final static String ROS_MASTER_URI = "http://10.183.122.142:11311/";
    //public final static String ROS_HOSTNAME = "10.183.63.119";  //IP ADDRESS OF CURRENT PC IN THE NETWORK

    public final static String ROS_MASTER_URI = "http://10.211.55.7:11311/";
    public final static String ROS_HOSTNAME = "10.211.55.2";

    ///Neo4J
    //public final static String NEO4J_ADDRESS = "bolt://10.183.122.142:7687";
    //public final static String NEO4J_USERNAME = "neo4j";
    //public final static String NEO4J_PASSWORD = "memory"; //do NOT commit password to the repo

    public final static String NEO4J_ADDRESS = "bolt://127.0.0.1:7687";
    public final static String NEO4J_USERNAME = "neo4j";
    public final static String NEO4J_PASSWORD = "root";

    ///Redis
    public final static String REDIS_URI = "redis://bot.roboy.org:6379/0";
    public final static String REDIS_PASSWORD = "root"; //do NOT commit password to the repo

    /** Available label and relationship types.
     * Used to validate the elements of the ROS queries.
     */
    public static final String[] LABEL_VALUES = new String[] { "Person","Robot","Company","University","City","Country","Hobby","Occupation","Object" };
    public static final String[] RELATION_VALUES = new String[] { "FRIEND_OF","LIVE_IN","FROM","WORK_FOR","STUDY_AT","MEMBER_OF","HAS_HOBBY","KNOW","IS","PART_OF","IS_IN" };


}
