package org.roboy.memory.util;

import org.apache.maven.shared.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {
    private StringBuilder builder;

    public QueryBuilder() {
        builder = new StringBuilder();
    }

    public QueryBuilder add(String text) {
        builder.append(" " + text + " ");
        return this;
    }

    public QueryBuilder addParameters(HashMap<String, String> params) {
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

    public QueryBuilder matchById(int id, String letter) {
        add("MATCH (" + letter + ") WHERE ID(" + letter + ")=" + id);
        return this;
    }

    public QueryBuilder set(HashMap<String, String> properties, String letter) {
        for (Map.Entry<String, String>  entry : properties.entrySet()) {
            add("SET " + letter + "." + entry.getKey() + "='" + entry.getValue() + "'");
        }
        return this;
    }

    public QueryBuilder add(String text, Object... args) {
        add(String.format(text, args));
        return this;
    }
}
