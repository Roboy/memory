package org.roboy.memory.models;

import java.time.LocalDateTime;

/** Data model for JSON parser.
 *  Creates objects, that contain the elements of the Header.
 */
public class Header {

    private String user; ///< Contains the module which is sending the query, for example "vision"
    private String datetime; ///< Contains a timestamp in seconds since 1.1.1970

    public LocalDateTime getDateTime() {return LocalDateTime.now();}

    public String getUser() {return user;}

}
