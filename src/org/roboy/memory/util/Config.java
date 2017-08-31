package org.roboy.memory.util;

/**
 * Configuration for ROS, Neo4J and Redis Server connectivity
 */
public class Config {
    //ROS Configuration
    public final static String ROS_MASTER_URI = "http://127.0.0.1:11311/"; ///< IP adress of the PC with roscore
    public final static String ROS_HOSTNAME = "127.0.0.1"; ///< IP address of the current PC in the network

    //Neo4J Configuration
    public final static String NEO4J_ADDRESS = "bolt://127.0.0.1:7687"; ///< Neo4j DB location
    public final static String NEO4J_USERNAME = "***"; ///< Neo4j instance username
    public final static String NEO4J_PASSWORD = "***";  ///< Neo4j instance password

    //Redis Configuration
    public final static String REDIS_URI = "redis://127.0.0.1:6379/0"; ///< Redis storage location
    public final static String REDIS_PASSWORD = "***"; ///< Redis storage instance password

    //KR Entries Configuration
    public static final String[] LABEL_VALUES = new String[] { "Person","Robot","Company","University","City","Country","Hobby","Occupation","Object" }; ///< Available label types
    public static final String[] RELATION_VALUES = new String[] { "FRIEND_OF","LIVE_IN","FROM","WORK_FOR","STUDY_AT","MEMBER_OF","HAS_HOBBY","KNOW","IS","PART_OF","IS_IN" }; ///< Available reltionship types
}
