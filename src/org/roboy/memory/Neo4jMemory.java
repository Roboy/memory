package org.roboy.memory;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.roboy.memory.interfaces.Neo4jMemoryInterface;
import org.roboy.memory.interfaces.Neo4jMemoryOperations;
import org.roboy.memory.models.MemoryNodeModel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Implements the high-level-querying tasks to the Memory services.
 */
public class Neo4jMemory implements Neo4jMemoryInterface {

    private Gson gson = new Gson();
    private final static Logger logger = LogManager.getLogger();

    public Neo4jMemory (){
        logger.info("Using Neo4jMemory");
    }

    /**
     * Updating information in the memory for an EXISTING node with known ID.
     *
     * @param node Node with a set ID, and other properties to be set or updated.
     * @return true for success, false for fail
     */
    @Override
    public boolean save(MemoryNodeModel node) throws InterruptedException, IOException
    {
        return Neo4jMemoryOperations.update(node);
    }

    /**
     * This query retrieves a a single node by its ID.
     *
     * @param  id the ID of requested
     * @return Node representation of the result.
     */
    public MemoryNodeModel getById(int id, Neo4jMemoryInterface memory) throws InterruptedException, IOException
    {
        return Neo4jMemoryOperations.getById(id, memory);
    }

    /**
     * This is a classical database query which finds all matching nodes.
     *
     * @param  query the ID of requested
     * @return Array of  IDs (all nodes which correspond to the pattern).
     */
    public ArrayList<MemoryNodeModel> getByQuery(MemoryNodeModel query, Neo4jMemoryInterface memory) throws InterruptedException, IOException
    {
        return Neo4jMemoryOperations.get(query, memory);
    }

    public int create(MemoryNodeModel query) throws InterruptedException, IOException
    {
        return Neo4jMemoryOperations.create(query);
    }

    /**
     * IF ONLY THE ID IS SET, THE NODE IN MEMORY WILL BE DELETED ENTIRELY.
     * Otherwise, the properties present in the query will be deleted.
     *
     * @param query StrippedQuery avoids accidentally deleting other fields than intended.
     */
    public boolean remove(MemoryNodeModel query) throws InterruptedException, IOException
    {
        query.setStripQuery(true);
        return Neo4jMemoryOperations.remove(query);
    }
}