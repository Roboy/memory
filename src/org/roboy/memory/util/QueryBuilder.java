package org.roboy.memory.util;

import org.apache.maven.shared.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {
    private StringBuilder builder;

    public QueryBuilder() {
        builder = new StringBuilder();
    }

    public void add(String text) {
        builder.append(" " + text + " ");
    }

    public void addParameters(HashMap<String, String> params) {
        String[] array = new String[params.size()];
        for (int i = 0; i < params.size(); i++) {
            array[i] = params.keySet().toArray()[i] + ":'" + params.values().toArray()[i] + "'";
        }
        add(" {" + StringUtils.join(array, ",") + "} ");
    }

    public String getQuery() {
        return builder.toString();
    }

    public void matchById(int id, String letter) {
        add(" MATCH (" + letter + ") WHERE ID(" + letter + ")=" + id + " ");
    }

    public void set(HashMap<String, String> properties, String letter) {
        for (Map.Entry<String, String>  entry : properties.entrySet()) {
            add(" SET " + letter + "." + entry.getKey() + "='" + entry.getValue() + "' ");
        }
    }

    public void add(String text, Object... args) {
        add(String.format(text, args));
    }
}
