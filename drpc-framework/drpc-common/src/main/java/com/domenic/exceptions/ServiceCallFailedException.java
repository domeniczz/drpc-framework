package com.domenic.exceptions;

/**
 * @author Domenic
 * @Classname ServiceCallFailedException
 * @Description Service Call Failed Exception
 * @Created by Domenic
 */
public class ServiceCallFailedException extends RuntimeException {

    public ServiceCallFailedException() {
        super();
    }

    public ServiceCallFailedException(String message) {
        super(message);
    }

    public ServiceCallFailedException(Throwable cause) {
        super(cause);
    }

}
