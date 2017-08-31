package org.roboy.memory.util;


import java.util.logging.Logger;

/**
 * Answer wrapper. Returns an OK or an error message to ROS.
 */
public class Answer {

    private static Logger logger = Logger.getLogger(Answer.class.toString());

    /** Answer for ROS that no errors were detected.
     *
     * @return Returns JSON object {status:"OK"} to ROS
     */
    public static String ok() {
        String result = "{status:\"OK\"}";
        logger.info(result);
        return result;
    }

    /** Answer for ROS that an error accured.
     *
     * @param message The error message
     * @return Returns JSON object containing status and message
     */
    public static String error(String message) {
        String result = "{status:\"FAIL\", message:\""+message+"\"}";
        logger.warning(result);
        return result;
    }
}
