package org.roboy.memory.util;

import java.util.logging.Logger;

/**
 * Answer wrapper. Outputs OK or error messages to ROS.
 */
public class Answer {

    private static Logger logger = Logger.getLogger(Answer.class.toString());

    /**
     * Answer for ROS if no errors were detected.
     *
     * @return JSON object {status:"OK"} to ROS
     */
    public static String ok() {
        String result = "{status:\"OK\"}"; ///< String with positive response
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
        String result = "{status:\"FAIL\", message:\""+message+"\"}"; ///< String with negative response
        logger.warning(result);
        return result;
    }
}
