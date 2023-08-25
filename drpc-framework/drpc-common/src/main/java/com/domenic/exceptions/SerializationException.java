package com.domenic.exceptions;

/**
 * @author Domenic
 * @Classname SerializationException
 * @Description Serialization Exception
 * @Created by Domenic
 */
public class SerializationException extends RuntimeException {

    public SerializationException() {
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }

}
