package org.roboy.memory.util;

/**
 * Configuration for ROS, Neo4J and Redis Server connectivity
 */

public class Config { //DO NOT CHANGE
    //ROS
    @Deprecated
    public final static String ROS_MASTER_URI = System.getenv("ROS_MASTER_URI") == null ? "http://localhost:11311/" : System.getenv("ROS_MASTER_URI");
    @Deprecated
    public final static String ROS_HOSTNAME = System.getenv("ROS_HOSTNAME") == null ? "127.0.0.1" : System.getenv("ROS_HOSTNAME");  //IP ADDRESS OF CURRENT PC IN THE NETWORK

    //Neo4J
    public final static String NEO4J_ADDRESS = System.getenv("NEO4J_ADDRESS") == null ? "bolt://localhost:7687" : System.getenv("NEO4J_ADDRESS");
    public final static String NEO4J_USERNAME = System.getenv("NEO4J_USERNAME") == null ? "neo4j" : System.getenv("NEO4J_USERNAME");
    public final static String NEO4J_PASSWORD = System.getenv("NEO4J_PASSWORD") == null ? "memory" : System.getenv("NEO4J_PASSWORD");

    //Redis
    public final static String REDIS_URI = System.getenv("REDIS_URI") == null ? "redis://localhost:6379/0" : System.getenv("REDIS_URI");
    public final static String REDIS_PASSWORD = System.getenv("REDIS_PASSWORD") == null ? "root" : System.getenv("REDIS_PASSWORD");
}

/* put your values here and copy to env
ROS_MASTER_URI=http://localhost:11311/
ROS_HOSTNAME=127.0.0.1
NEO4J_ADDRESS=bolt://localhost:7687
NEO4J_USERNAME=neo4j
NEO4J_PASSWORD=memory
REDIS_URI=redis://localhost:6379/0
REDIS_PASSWORD=root
 */