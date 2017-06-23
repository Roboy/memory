package memory.services;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.node.service.ServiceResponseBuilder;
import roboy_communication_cognition.*;


/**
 * ROS Service for saving data object to DB. Data is received as JSON object.
 * JSON object is parsed using Parser (not implemented) and saved to neo4j.
 */
public class SaveObject extends AbstractNodeMain {
    private static String name = "/roboy/cognition/memory/data/save";

    public static void publish(NodeConfiguration nodeConfiguration, NodeMainExecutor nodeMainExecutor) {
        nodeConfiguration.setNodeName(name);
        nodeMainExecutor.execute(new SaveObject(), nodeConfiguration);
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(name);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        connectedNode.newServiceServer(this.getDefaultNodeName(), roboy_communication_cognition.SaveObject._TYPE,
                (ServiceResponseBuilder<SaveObjectRequest, SaveObjectResponse>) (request, response) -> {
                    //logic here
                    response.setResult(true);
                });
    }
}
