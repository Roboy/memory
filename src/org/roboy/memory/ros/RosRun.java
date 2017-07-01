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

    private NodeMainExecutor nodeMainExecutor;
    private NodeConfiguration nodeConfiguration;

    public RosRun() throws URISyntaxException {
        URI masterUri = new URI(Config.ROS_MASTER_URI);
        nodeConfiguration = NodeConfiguration.newPublic(Config.ROS_HOSTNAME, masterUri);
        nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
    }


    public void start() {
        //register ros node with services
        RosNode.register(nodeConfiguration, nodeMainExecutor);
    }

    public void stop() {
        nodeMainExecutor.shutdown();
    }

}
