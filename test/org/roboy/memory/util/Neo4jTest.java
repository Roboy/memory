package org.roboy.memory.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.roboy.memory.models.*;

import java.lang.reflect.Type;
import java.util.Date;

public class Neo4jTest extends TestCase {

    Gson gson = new Gson();
    long timestamp = new Date().getTime();

    // public void testRun() throws Exception {
    //     String json = Neo4j.run("MATCH (n:Robot) RETURN n");
    //     JsonArray array = gson.fromJson(json, JsonArray.class);
    //     assertEquals(1, array.size());
    //     String roboy = array.get(0).toString();
    //     assertEquals(true, roboy.contains("Roboy"));
    // }

    public void testCreateNode() throws Exception {
        Create create = gson.fromJson("{'label':'Person','properties':{'name':'Lucas', 'sex':'male', 'timestamp_test':'" + timestamp + "'}}", Create.class);
        int id = gson.fromJson(Neo4j.createNode(create), JsonObject.class).get("id").getAsInt();
        assertTrue(id > 0);
    }

//    public void testCreateNodeWithFace() throws Exception {
//        Create create = gson.fromJson("{'label':'Person','properties':{'name':'Lucas', 'sex':'male', 'timestamp_test':'"+timestamp+"'}}", Create.class);
//        int id = gson.fromJson(Neo4j.createNode(create), JsonObject.class).get("id").getAsInt();
//        assertTrue(id > 0);
//    }

    public void testUpdateNode() throws Exception {
        Create create = gson.fromJson("{'label':'Person','properties':{'name':'Lucas', 'sex':'male', 'timestamp_test':'" + timestamp + "'}}", Create.class);
        int id = gson.fromJson(Neo4j.createNode(create), JsonObject.class).get("id").getAsInt();
        Create createRob = gson.fromJson("{'label':'Robot','properties':{'name':'Roboy', 'timestamp_test':'" + timestamp + "'}}", Create.class);
        int idRob = gson.fromJson(Neo4j.createNode(createRob), JsonObject.class).get("id").getAsInt();
        Update update = gson.fromJson("{'type':'node','id':" + id + ",'properties':{'surname':'Ki', 'xyz':'abc'}, 'relationships':{'FRIEND_OF':[" + idRob + "]}}", Update.class);
        JsonObject answer = gson.fromJson(Neo4j.updateNode(update), JsonObject.class);
        assertTrue(answer.get("properties updated").getAsBoolean());
        assertTrue(answer.get("relationships created").getAsInt() > 0);
    }

    public void testGetNodeById() throws Exception {
        Create create = gson.fromJson("{'label':'Person','properties':{'name':'Lucas', 'sex':'male', 'timestamp_test':'" + timestamp + "'}}", Create.class);
        int id = gson.fromJson(Neo4j.createNode(create), JsonObject.class).get("id").getAsInt();
        Node node = gson.fromJson(Neo4j.getNodeById(id), Node.class);
        assertEquals(id, (int)node.getId());
        assertEquals(3, node.getProperties().size());
    }

    public void testGetNode() throws Exception {
        Create create = gson.fromJson("{'label':'Person','properties':{'name':'Lucas', 'sex':'male', 'timestamp_test':'" + timestamp + "'}}", Create.class);
        int id = gson.fromJson(Neo4j.createNode(create), JsonObject.class).get("id").getAsInt();
        Get get = new Get();
        get.setProperties(create.getProperties());
        get.setLabel(create.getLabel());
        JsonObject node = gson.fromJson(Neo4j.getNode(get), JsonObject.class);
        assertEquals(id, node.get("id").getAsJsonArray().get(0).getAsInt());
    }

    public void testRemove() throws Exception {
        Create create = gson.fromJson("{'label':'Person','properties':{'name':'Lucas', 'sex':'male', 'timestamp_test':'" + timestamp + "'}}", Create.class);
        int id = gson.fromJson(Neo4j.createNode(create), JsonObject.class).get("id").getAsInt();
        Create createFriend = gson.fromJson("{'label':'Person','properties':{'name':'Tobias', 'sex':'male', 'timestamp_test':'" + timestamp + "'}}", Create.class);
        int idFriend = gson.fromJson(Neo4j.createNode(createFriend), JsonObject.class).get("id").getAsInt();
        Update update = gson.fromJson("{'type':'node','id':" + id + ", 'relationships':{'FRIEND_OF':[" + idFriend + "]}}", Update.class);
        Neo4j.updateNode(update);
        Remove remove = gson.fromJson("{'type':'node','id':" + id + ",'properties_list': ['sex'], 'relationships':{'FRIEND_OF':[" + idFriend + "]}}", Remove.class);
        Neo4j.remove(remove);
        Node node = gson.fromJson(Neo4j.getNodeById(id), Node.class);
        assertEquals(id, (int)node.getId());
        assertEquals(null, node.getProperties().get("sex"));
        assertEquals(null, node.getRelationships());
    }

    public void tearDown() throws Exception {
        Neo4j.run("MATCH (n{timestamp_test:'" + timestamp + "'}) DETACH DELETE n");
    }
}