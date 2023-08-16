package com.domenic.exceptions;

/**
 * @author Domenic
 * @Classname DiscoveryException
 * @Description Discovery Exception
 * @Created by Domenic
 */
public class DiscoveryException extends RuntimeException {

    public DiscoveryException() {
    }

    public DiscoveryException(String message) {
        super(message);
    }

    public DiscoveryException(Throwable cause) {
        super(cause);
    }

}
