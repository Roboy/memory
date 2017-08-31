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


    private NodeMainExecutor nodeMainExecutor; //*< ROS executor
    private NodeConfiguration nodeConfiguration; //*< ROS node configuration

    /** Constructor
     *
     */
    public RosRun() throws URISyntaxException {
        URI masterUri = new URI(Config.ROS_MASTER_URI);
        nodeConfiguration = NodeConfiguration.newPublic(Config.ROS_HOSTNAME, masterUri);
        nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
    }

    /** Register ros node with services.
     *
     */
    public void start() {
        RosNode.register(nodeConfiguration, nodeMainExecutor);
    }

    public void stop() {
        nodeMainExecutor.shutdown();
    }

}
