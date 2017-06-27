package memory;


import memory.services.ReadData;
import memory.services.WriteData;
import memory.util.Config;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Responsible for starting ROS Nodes. Uses configuration from Config class.
 * All nodes should be published in start method.
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
        ReadData.publish(nodeConfiguration, nodeMainExecutor);
        WriteData.publish(nodeConfiguration, nodeMainExecutor);
    }

    public void stop() {
        nodeMainExecutor.shutdown();
    }

}
