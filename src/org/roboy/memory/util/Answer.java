package org.roboy.memory.util;


/**
 * answer wrapper
 * to be implemented ...
 */
public class Answer {

    public static String ok() {
        return "{status:\"OK\"}";
    }

    public static String error(String message) {

        return "{status:\"FAIL\", message:\""+message+"\"}";
    }
}
