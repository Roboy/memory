package org.roboy.memory.util;

import org.apache.maven.shared.utils.StringUtils;
import org.roboy.ontology.Neo4jProperty;

import java.util.HashMap;
import java.util.Map;

public class NativeQueryBuilder {
    private StringBuilder builder;

    public NativeQueryBuilder() {
        builder = new StringBuilder();
    }

    public NativeQueryBuilder add(String text) {
        builder.append(" ").append(text).append(" ");
        return this;
    }

    public NativeQueryBuilder addParameters(HashMap<Neo4jProperty, Object> params) {
        if(params == null) return this;
        String[] array = new String[params.size()];
        for (int i = 0; i < params.size(); i++) {
            array[i] = params.keySet().toArray()[i] + ":'" + params.values().toArray()[i] + "'";
        }
        add("{%s}", StringUtils.join(array, ","));

        return this;
    }

    public String getQuery() {
        return builder.toString();
    }

    public NativeQueryBuilder matchById(int id, String letter) {
        add("MATCH (" + letter + ") WHERE ID(" + letter + ")=" + id);
        return this;
    }

    public NativeQueryBuilder set(HashMap<Neo4jProperty, Object> properties, String letter) {
        for (Map.Entry<Neo4jProperty, Object>  entry : properties.entrySet()) {
            add("SET " + letter + "." + entry.getKey().type + "='" + entry.getValue() + "'");
        }
        return this;
    }

    public NativeQueryBuilder add(String text, Object... args) {
        add(String.format(text, args));
        return this;
    }
}
