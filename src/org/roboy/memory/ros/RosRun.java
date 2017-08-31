package org.roboy.memory.ros;


import org.roboy.memory.util.Config;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * This server is responsible for starting ros services.
 */
public class RosRun {


    private NodeMainExecutor nodeMainExecutor; ///< ROS executor
    private NodeConfiguration nodeConfiguration; ///< ROS node configurator

    /**
     * Constructor.
     * Initializes the ROS node.
     */
    public RosRun() throws URISyntaxException {
        URI masterUri = new URI(Config.ROS_MASTER_URI); ///< URI of the PC running roscore
        nodeConfiguration = NodeConfiguration.newPublic(Config.ROS_HOSTNAME, masterUri); ///< Configuration of the ROS node with current PC address and master address
        nodeMainExecutor = DefaultNodeMainExecutor.newDefault(); ///< Executing the ROS node
    }

    /**
     * Registers the ROS node with services in the network.
     */
    public void start() {
        RosNode.register(nodeConfiguration, nodeMainExecutor);
    }

    /**
     * Shutdowns the ROS node and terminates the services.
     */
    public void stop() {
        nodeMainExecutor.shutdown();
    }

}
