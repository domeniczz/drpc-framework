package com.domenic.transport.message;

/**
 * @author Domenic
 * @Classname Types
 * @Description Type values used in message
 * @Created by Domenic
 */
public class Types {

    /**
     * Request type enum
     */
    public enum RequestType {

        REQUEST((byte) 1, "Normal Request"), HEARTBEAT((byte) 2, "Heart Beat");

        private byte id;
        private String type;

        RequestType(byte id, String type) {
            this.id = id;
            this.type = type;
        }

        public byte getId() {
            return id;
        }

        public String getType() {
            return type;
        }

    }

}
