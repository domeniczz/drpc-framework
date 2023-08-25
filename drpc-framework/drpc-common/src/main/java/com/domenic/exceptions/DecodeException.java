package com.domenic.exceptions;

/**
 * @author Domenic
 * @Classname DecodeException
 * @Description Decode Exception
 * @Created by Domenic
 */
public class DecodeException extends RuntimeException {

    public DecodeException() {
    }

    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(Throwable cause) {
        super(cause);
    }

}
