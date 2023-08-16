package com.domenic.exceptions;

/**
 * @author Domenic
 * @Classname NetworkException
 * @Description Network Exception
 * @Created by Domenic
 */
public class NetworkException extends RuntimeException {

    public NetworkException() {
    }

    public NetworkException(String messgae) {
        super(messgae);
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }
}
