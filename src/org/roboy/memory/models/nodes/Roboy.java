package org.roboy.memory.models.nodes;

import com.google.gson.Gson;
import org.roboy.memory.interfaces.Neo4jMemoryInterface;
import org.roboy.memory.models.MemoryNodeModel;
import org.roboy.ontology.Neo4jLabel;
import org.roboy.ontology.Neo4jProperty;
import org.roboy.ontology.constraints.RoboyConstraints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;


/**
 * Encapsulates a NodeModel and enables dialog states to easily store
 * and retrieve information about Roboy.
 */
public class Roboy extends MemoryNodeModel {
    final private static Logger LOGGER = Logger.getLogger(MemoryNodeModel.class.toString());

    /**
     * Initializer for the Roboy node
     */
    public Roboy(Neo4jMemoryInterface memory, String name) {
        super(true, memory);
        this.InitializeRoboy(name);
    }

    /**
     * Default initializer for the Roboy node
     */
    public Roboy(Neo4jMemoryInterface memory) {
        super(true, memory);
        this.InitializeRoboy("roboy two");
    }

    private void setOntologyConditions() {
        this.setNeo4jLegalLabels(RoboyConstraints.legalLabels);
        this.setNeo4jLegalRelationships(RoboyConstraints.legalRelationships);
        this.setNeo4jLegalProperties(RoboyConstraints.legalPropeties);
    }


    /**
     * This method initializes the roboy property as a node that
     * is in sync with memory and represents the Roboy itself.
     *
     * If something  goes wrong during querying, Roboy stays empty
     * and soulless, and has to fallback
     */
    // TODO consider a fallback for the amnesia mode
    private void InitializeRoboy(String name) {
        setProperties(Neo4jProperty.name, name);
        setLabel(Neo4jLabel.Robot);

        //
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            ids = memory.getByQuery(this);
        } catch (InterruptedException | IOException e) {
            LOGGER.severe("Cannot retrieve or find Roboy in the Memory. Go the amnesia mode: " + e.getMessage());
        }
        // Pick the first if matches found.
        if (ids != null && !ids.isEmpty()) {
            try {
                for (Integer id : ids) {
                    MemoryNodeModel node = memory.getById(id);
                    if (node.getProperties() != null &&
                            !node.getProperties().isEmpty() &&
                            node.getProperties().containsKey(Neo4jProperty.name) &&
                            node.getProperties().get(Neo4jProperty.name).equals(name)) {
                        this.set(node);
                        FAMILIAR = true;
                        break;
                    }
                }
            } catch (InterruptedException | IOException e) {
                LOGGER.severe("Unexpected error: provided ID not found. Go the amnesia mode: " + e.getMessage());
            }
        }
    }
}
