package org.roboy.memory.models;

import com.google.gson.annotations.Expose;
import org.roboy.memory.interfaces.Neo4jMemoryInterface;
import org.roboy.memory.util.DummyMemory;
import org.roboy.ontology.Neo4jLabel;
import org.roboy.ontology.Neo4jProperty;
import org.roboy.ontology.Neo4jRelationship;
import org.roboy.ontology.NodeModel;

import java.io.IOException;
import java.util.*;
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

    public MemoryNodeModel(){
        super();
        this.memory = null;
    }

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

    public HashSet<Neo4jLabel> getNeo4jLegalLabels() {
        return Neo4jLegalLabels;
    }

    protected void setNeo4jLegalLabels(HashSet<Neo4jLabel> neo4jLegalLabels) {
        Neo4jLegalLabels = neo4jLegalLabels;
    }

    public HashSet<Neo4jRelationship> getNeo4jLegalRelationships() {
        return Neo4jLegalRelationships;
    }

    protected void setNeo4jLegalRelationships(HashSet<Neo4jRelationship> neo4jLegalRelationships) {
        Neo4jLegalRelationships = neo4jLegalRelationships;
    }

    public HashSet<Neo4jProperty> getNeo4jLegalProperties() {
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
            } else {
                initialized = false;
            }
        }
        return false;
    }

    private MemoryNodeModel queryForMatchingNodes(MemoryNodeModel node) {
        if (memory != null) {
            if (node.getLabel() != null || node.getLabels() != null) {
                ArrayList<MemoryNodeModel> nodes = new ArrayList<>();
                // Query memory for matching nodes.
                try {
                    nodes = memory.getByQuery(node, memory);
                } catch (InterruptedException | IOException e) {
                    LOGGER.info("Exception while querying memory, assuming node unknown. " + e.getMessage());
                }
                // Pick first if matches found.
                if (nodes != null && !nodes.isEmpty()) {
                    //TODO Change from using first id to specifying if multiple matches are found.
                    return nodes.get(0);
                }
            }
        }
        return null;
    }

    private MemoryNodeModel create(MemoryNodeModel node) {
        if (memory != null) {
            if(!(memory instanceof DummyMemory)) {
                try {
                    int id = memory.create(node);
                    // Need to retrieve the created node by the id returned by memory
                    return memory.getById(id, memory);
                } catch (InterruptedException | IOException e) {
                    LOGGER.warning("Unexpected memory error: provided ID not found upon querying.");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Adds a new relationship to the node, updating memory.
     */
    public boolean addInformation(Neo4jRelationship relationship, String name) {
        if (memory != null) {
            ArrayList<MemoryNodeModel> nodes = new ArrayList<>();
            // First check if node with given name exists by a matching query.
            MemoryNodeModel relatedNode = new MemoryNodeModel(true, memory);
            relatedNode.setProperties(Neo4jProperty.name, name);
            //This adds a label type to the memory query depending on the relation.
            relatedNode.setLabel(Neo4jRelationship.determineNodeType(relationship));
            try {
                nodes = memory.getByQuery(relatedNode, memory);
            } catch (InterruptedException | IOException e) {
                LOGGER.severe("Exception while querying memory by template. " + e.getMessage());
                return false;
            }
            // Pick first from list if multiple matches found.
            if (nodes != null && !nodes.isEmpty()) {
                //TODO Change from using first id to specifying if multiple matches are found.
                setRelationships(relationship, nodes.get(0).getId());
            }
            // Create new node if match is not found.
            else {
                try {
                    int id = memory.create(relatedNode);
                    if (id != 0) { // 0 is default value, returned if Memory response was FAIL.
                        setRelationships(relationship, id);
                    }
                } catch (InterruptedException | IOException e) {
                    LOGGER.severe("Unexpected memory error: creating node for new relation failed. " + e.getMessage());
                    return false;
                }
            }
            //Update the person node in memory.
            try {
                memory.save(this);
            } catch (InterruptedException | IOException e) {
                LOGGER.severe("Unexpected memory error: updating person information failed. " + e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    public void setStripQuery(boolean strip) {
        this.stripQuery = strip;
    }

    @Override
    public boolean isLegal() {
        // TODO: implement
        return true;
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
