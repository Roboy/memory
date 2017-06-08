package memory;


import memory.services.*;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * This server is responsible for starting ros services.
 */
public class Server {

    private NodeConfiguration nodeConfiguration;
    private NodeMainExecutor nodeMainExecutor;

    public Server() throws URISyntaxException {
        URI masterUri = new URI(Config.ROS_MASTER_ADDRESS);

        nodeConfiguration = NodeConfiguration.newPublic(masterUri.getHost(), masterUri);
        nodeMainExecutor = DefaultNodeMainExecutor.newDefault();

    }

    public void start() {
        //TODO(alex): run all classes from services package using reflection
        SaveObject.publish(nodeConfiguration, nodeMainExecutor);
    }

    public void stop() {
        nodeMainExecutor.shutdown();
    }

}
