package org.roboy.memory.models;

@Deprecated
public abstract class RosNode extends Node {

    private transient String error;

    protected void error(String text) {
        error = text;
    }

    public String getError() {
        return error;
    }

    public abstract boolean validate();
}
