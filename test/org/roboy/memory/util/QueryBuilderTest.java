package org.roboy.memory.util;

import junit.framework.TestCase;

import java.util.HashMap;

public class QueryBuilderTest extends TestCase {

    QueryBuilder builder;

    public void setUp() throws Exception {
        builder = new QueryBuilder();
    }

    public void testAdd() throws Exception {
        builder.add("test");
        assertEquals(" test ", builder.getQuery());
    }

    public void testAddParameters() throws Exception {
        HashMap<String, String> var1 = new HashMap<>();
        var1.put("test", "value");
        var1.put("test1", "value1");
        String result = " {test:'value',test1:'value1'} ";
        builder.addParameters(var1);
        assertEquals(result, builder.getQuery());
    }

    public void testMatchById() throws Exception {
        String result = " MATCH (n) WHERE ID(n)=1 ";
        builder.matchById(1, "n");
        assertEquals(result, builder.getQuery());
    }

    public void testSet() throws Exception {
        HashMap<String, String> var1 = new HashMap<>();
        var1.put("test", "value");
        var1.put("test1", "value1");
        String result = " SET n.test='value'  SET n.test1='value1' ";
        builder.set(var1, "n");
        assertEquals(result, builder.getQuery());
    }

    public void testAddFormated() throws Exception {
        builder.add("%s", "test");
        assertEquals(" test ", builder.getQuery());
    }
}