package memory;


/**
 * Configuration variables
 */
class Config {
    //ROS
    final static String ROS_MASTER_ADDRESS = "http://192.168.56.101:11311/";
    //Neo4j
    final static String NEO4J_ADDRESS = "bolt://85.10.197.57:7687";
    final static String NEO4J_USERNAME = "memory";
    final static String NEO4J_PASSWORD = "*********"; //TODO: don't commit password to the repo

}
