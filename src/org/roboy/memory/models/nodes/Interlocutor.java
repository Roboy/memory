package org.roboy.memory.models.nodes;

import org.roboy.memory.interfaces.Neo4jMemoryInterface;
import org.roboy.memory.models.MemoryNodeModel;
import org.roboy.ontology.Neo4jLabel;
import org.roboy.ontology.Neo4jProperty;
import org.roboy.ontology.constraints.InterlocutorConstraints;
import org.roboy.ontology.util.Uuid;
import org.roboy.ontology.util.UuidType;
import org.roboy.ontology.util.UzupisIntents;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Encapsulates a NodeModel and enables dialog states to easily store
 * and retrieve information about its current conversation partner.
 */
public class Interlocutor extends MemoryNodeModel {
    final private static Logger LOGGER = Logger.getLogger(MemoryNodeModel.class.toString());

    private HashMap<UzupisIntents, String> uzupisInfo = new HashMap<>();

    public Interlocutor(Neo4jMemoryInterface memory) {
        super(memory);
        this.setOntologyConditions();
    }

    public Interlocutor(Neo4jMemoryInterface memory, String name) {
        super(memory);
        this.addName(name);
        this.setOntologyConditions();
    }

    public Interlocutor(Neo4jMemoryInterface memory, Uuid uuid) {
        super(memory);
        this.addUuid(uuid);
        this.setOntologyConditions();
    }

    private void setOntologyConditions() {
        this.setNeo4jLegalLabels(InterlocutorConstraints.legalLabels);
        this.setNeo4jLegalRelationships(InterlocutorConstraints.legalRelationships);
        this.setNeo4jLegalProperties(InterlocutorConstraints.legalPropeties);
    }

    /**
     * After executing this method, the person field contains a node that
     * is in sync with memory and represents the interlocutor.
     *
     * Unless something goes wrong during querying, which would affect the
     * following communication severely.
     */
    public void addName(String name) {
        setProperties(Neo4jProperty.name, name);
        setLabel(Neo4jLabel.Person);
        FAMILIAR = this.init(this);
    }

    public void addUuid(Uuid uuid) {
        setProperties(uuid.getType().toNeo4jProperty(), uuid);
        setLabel(uuid.getType().toNeo4jLabel());
        FAMILIAR = this.init(this);
    }

    public Uuid getUuid(UuidType type) {
        return new Uuid(type, (String) getProperties(type.toNeo4jProperty()));
    }

    public void saveUzupisProperty(UzupisIntents intent, String value) {
        uzupisInfo.put(intent, value);
    }

    public HashMap<UzupisIntents, String> getUzupisInfo() {
        return uzupisInfo;
    }
}