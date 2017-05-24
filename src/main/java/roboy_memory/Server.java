package roboy_memory;


import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import roboy_memory.services.vision.AddTwoInts;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * This server is responsible for starting ros services.
 */
public class Server {

    private NodeConfiguration nodeConfiguration;
    private NodeMainExecutor nodeMainExecutor;

    public Server() throws URISyntaxException {
        URI masterUri = new URI(Config.DEFAULT_MASTER_ADDRESS);

        nodeConfiguration = NodeConfiguration.newPublic(masterUri.getHost(), masterUri);
        nodeMainExecutor = DefaultNodeMainExecutor.newDefault();

    }

    public void start() {
        AddTwoInts.publish(nodeConfiguration, nodeMainExecutor);
    }

    public void stop() {
        nodeMainExecutor.shutdown();
    }

}
