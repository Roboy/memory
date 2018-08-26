package org.roboy.memory.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import org.roboy.memory.interfaces.Neo4jMemoryInterface;
import org.roboy.memory.models.nodes.Interlocutor;
import org.roboy.memory.util.DummyMemory;
import org.roboy.ontology.Neo4jProperty;
import org.roboy.ontology.Neo4jRelationship;
import org.roboy.ontology.NodeModel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class represents a full node similarly to its representation in Memory.
 */
public class MemoryNodeModel extends NodeModel {
    final private static Logger LOGGER = Logger.getLogger(MemoryNodeModel.class.toString());
    protected Neo4jMemoryInterface memory;

    protected boolean initialized = false;
    protected boolean FAMILIAR = false;
    //If true, then fields with default values will be removed from JSON format.
    // Transient as stripping information is not a part of the node and not included in query.
    @Expose
    private transient boolean stripQuery = false;

    public MemoryNodeModel(Neo4jMemoryInterface memory){
        super();
        this.memory = memory;
    }

    public MemoryNodeModel(MemoryNodeModel node, Neo4jMemoryInterface memory){
        super(node);
        this.memory = memory;
        this.set(node);
    }

    public MemoryNodeModel(boolean stripQuery, Neo4jMemoryInterface memory) {
        super();
        this.memory = memory;
        if(!stripQuery) {
            this.resetNode();
        } else {
            this.setId(0);
            this.stripQuery = true;
        }
    }

    protected HashSet<Neo4jLabel> getNeo4jLegalLabels() {
        return Neo4jLegalLabels;
    }

    protected void setNeo4jLegalLabels(HashSet<Neo4jLabel> neo4jLegalLabels) {
        Neo4jLegalLabels = neo4jLegalLabels;
    }

    protected HashSet<Neo4jRelationship> getNeo4jLegalRelationships() {
        return Neo4jLegalRelationships;
    }

    protected void setNeo4jLegalRelationships(HashSet<Neo4jRelationship> neo4jLegalRelationships) {
        Neo4jLegalRelationships = neo4jLegalRelationships;
    }

    protected HashSet<Neo4jProperty> getNeo4jLegalProperties() {
        return Neo4jLegalProperties;
    }

    protected void setNeo4jLegalProperties(HashSet<Neo4jProperty> neo4jLegalProperties) {
        Neo4jLegalProperties = neo4jLegalProperties;
    }

    public boolean isFamiliar() {
        if (!initialized) {
            FAMILIAR = init(this);
        }
        return FAMILIAR;
    }

    protected boolean init(MemoryNodeModel node) {
        MemoryNodeModel result = this.queryForMatchingNodes(node);
        initialized = true;
        if (result != null) {
            this.set(result);
            return true;
        } else {
            result = this.create(node);
            if (result != null) {
                this.set(result);
            }
        }
        return false;
    }

    private MemoryNodeModel queryForMatchingNodes(MemoryNodeModel node) {
        if (node.getLabel() != null || node.getLabels() != null) {
            ArrayList<Integer> ids = new ArrayList<>();
            // Query memory for matching nodes.
            try {
                ids = memory.getByQuery(node);
            } catch (InterruptedException | IOException e) {
                LOGGER.info("Exception while querying memory, assuming node unknown.");
                e.printStackTrace();
            }
            // Pick first if matches found.
            if (ids != null && !ids.isEmpty()) {
                //TODO Change from using first id to specifying if multiple matches are found.
                try {
                    return fromJSON(memory.getById(ids.get(0)), new Gson());
                } catch (InterruptedException | IOException e) {
                    LOGGER.warning("Unexpected memory error: provided ID not found upon querying.");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private MemoryNodeModel create(MemoryNodeModel node) {
        if(!(memory instanceof DummyMemory)) {
            try {
                int id = memory.create(node);
                // Need to retrieve the created node by the id returned by memory
                return fromJSON(memory.getById(id), new Gson());
            } catch (InterruptedException | IOException e) {
                LOGGER.warning("Unexpected memory error: provided ID not found upon querying.");
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Adds a new relationship to the node, updating memory.
     */
    public void addInformation(Neo4jRelationship relationship, String name) {
        ArrayList<Integer> ids = new ArrayList<>();
        // First check if node with given name exists by a matching query.
        MemoryNodeModel relatedNode = new MemoryNodeModel(true, memory);
        relatedNode.setProperties(Neo4jProperty.name, name);
        //This adds a label type to the memory query depending on the relation.
        relatedNode.setLabel(Neo4jRelationship.determineNodeType(relationship));
        try {
            ids = memory.getByQuery(relatedNode);
        } catch (InterruptedException | IOException e) {
            LOGGER.severe("Exception while querying memory by template.");
            e.printStackTrace();
        }
        // Pick first from list if multiple matches found.
        if(ids != null && !ids.isEmpty()) {
            //TODO Change from using first id to specifying if multiple matches are found.
            setRelationships(relationship, ids.get(0));
        }
        // Create new node if match is not found.
        else {
            try {
                int id = memory.create(relatedNode);
                if(id != 0) { // 0 is default value, returned if Memory response was FAIL.
                    setRelationships(relationship, id);
                }
            } catch (InterruptedException | IOException e) {
                LOGGER.severe("Unexpected memory error: creating node for new relation failed.");
                e.printStackTrace();
            }
        }
        //Update the person node in memory.
        try{
            memory.save(this);
        } catch (InterruptedException | IOException e) {
            LOGGER.severe("Unexpected memory error: updating person information failed.");
            e.printStackTrace();
        }
    }

    public void setStripQuery(boolean strip) {
        this.stripQuery = strip;
    }

    /**
     * This toString method returns the whole object, including empty variables.
     */
    public String toJSON(){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json;
        // ID of 0 is default ID for an uncertain node.
        // If there exists a valid non-default ID value obtained from memory, it has to be included
        if (getId() != 0) {
            JsonElement jsonElement = gson.toJsonTree(this);
            jsonElement.getAsJsonObject().addProperty("id", getId());
            json = gson.toJson(jsonElement);
        } else {
            json = gson.toJson(this);
        }
        if(stripQuery) {
            //This is based on https://stackoverflow.com/questions/23920740/remove-empty-collections-from-a-json-with-gson
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String,Object> obj = gson.fromJson(json, type);
            for(Iterator<Map.Entry<String, Object>> it = obj.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, Object> entry = it.next();
                if (entry.getValue() == null) {
                    it.remove();
                } else if (entry.getValue().getClass().equals(ArrayList.class)) {
                    if (((ArrayList<?>) entry.getValue()).size() == 0) {
                        it.remove();
                    }
                    //As ID is parsed into Double inside GSON, usng Double.class
                } else if (entry.getValue().getClass().equals(Double.class)) {
                    if (((Double) entry.getValue()) == 0) {
                        it.remove();
                    }
                } else if (entry.getValue().getClass().equals(HashMap.class)) {
                    if (((HashMap<?,?>) entry.getValue()).size() == 0) {
                        it.remove();
                    }
                } else if (entry.getValue().getClass().equals(String.class)) {
                    if (((String) entry.getValue()).equals("")) {
                        it.remove();
                    }
                }
            }
            json = gson.toJson(obj);
        }
        return json;
    }
    /**
     * Returns an instance of this class based on the given JSON.
     */
    public MemoryNodeModel fromJSON(String json, Gson gson) {
        return gson.fromJson(json, this.getClass());
    }

    @Override
    public String toString() {
        return "NodeModel{" +
                "memory=" + memory +
                ", id=" + getId() +
                ", label=" + getLabel() +
                ", labels=" + getLabels() +
                ", properties=" + getProperties() +
                ", relationships=" + getRelationships() +
                ", stripQuery=" + stripQuery +
                '}';
    }
}
