package org.roboy.memory.util;


import java.util.logging.Logger;

/**
 * answer wrapper
 */
public class Answer {

    private static Logger logger = Logger.getLogger(Answer.class.toString());


    public static String ok(String message) {
        String result = "{\"status\":\"OK\", \"message\":\""+message+"\"}";
        logger.info(result);
        return result;
    }

    public static String error(String message) {
        String result = "{\"status\":\"FAIL\", \"message\":\""+message+"\"}";
        logger.warning(result);
        return result;
    }
}
