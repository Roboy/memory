package org.roboy.memory.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import org.roboy.memory.interfaces.Neo4jMemoryInterface;
import org.roboy.ontology.NodeModel;

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
    final private static Logger logger = Logger.getLogger(MemoryNodeModel.class.toString());
    protected Neo4jMemoryInterface memory;

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
        super();
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

    public boolean isFamiliar() {
        return FAMILIAR;
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
