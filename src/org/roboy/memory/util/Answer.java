package org.roboy.memory.util;

import java.util.logging.Logger;

/**
 * Answer wrapper. Outputs OK or error messages to ROS.
 */
@Deprecated
public class Answer {

    private static Logger logger = Logger.getLogger(Answer.class.toString());

    /**
     * Answer for ROS if no errors were detected.
     *
     * @param message contains the success message with json data
     * @return JSON object {status:"OK"} to ROS
     */
    public static String ok(String message) {
        String result = "{\"status\":\"OK\", \"message\":\""+message+"\"}";
        logger.info(result);
        return result;
    }

    /**
     * Answer for ROS if an error occurred.
     *
     * @param message contains the error message according to the obstacle approached
     * @return JSON object containing status and message
     */
    public static String error(String message) {
        String result = "{\"status\":\"FAIL\", \"message\":\""+message+"\"}";
        logger.warning(result);
        return result;
    }
}
