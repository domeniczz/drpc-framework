package com.domenic.exceptions;

/**
 * @author Domenic
 * @Classname ZookeeperException
 * @Description Zookeeper exception
 * @Created by Domenic
 */
public class ZookeeperException extends RuntimeException {

    public ZookeeperException() {
    }

    public ZookeeperException(String message) {
        super(message);
    }

    public ZookeeperException(Throwable cause) {
        super(cause);
    }

}
