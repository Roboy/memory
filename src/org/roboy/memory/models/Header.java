package org.roboy.memory.models;

import java.time.LocalDateTime;

public class Header {

    private String user;
    private String datetime;

    public LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }

}
