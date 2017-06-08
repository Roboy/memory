package memory.services;

import org.ros.namespace.GraphName;
import org.ros.node.*;
import org.ros.node.service.ServiceResponseBuilder;
import rosjava_test_msgs.AddTwoIntsRequest;
import rosjava_test_msgs.AddTwoIntsResponse;

/**
 * Example
 */
public class AddTwoInts extends AbstractNodeMain {
    private static String name = "rosjava_tutorial_services/server";

    public static void publish(NodeConfiguration nodeConfiguration, NodeMainExecutor nodeMainExecutor) {
        nodeConfiguration.setNodeName(name);
        nodeMainExecutor.execute(new AddTwoInts(), nodeConfiguration);
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(name);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        connectedNode.newServiceServer("add_two_ints", rosjava_test_msgs.AddTwoInts._TYPE,
                (ServiceResponseBuilder<AddTwoIntsRequest, AddTwoIntsResponse>) (request, response) -> {
                    response.setSum(request.getA() + request.getB());
                });
    }
}
