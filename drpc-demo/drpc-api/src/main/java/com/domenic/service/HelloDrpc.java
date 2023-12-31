package com.domenic.service;

/**
 * @author Domenic
 * @Classname HelloDrpc
 * @Description Hello Drpc Service Interface
 * @Created by Domenic
 */
public interface HelloDrpc {

    /**
     * Generic API
     * @param msg message to send
     * @return return value
     */
    String sayHello(String msg);

}
