package com.domenic.impl;

import com.domenic.HelloDrpc;

/**
 * @author Domenic
 * @Classname HelloDrpcImpl
 * @Description TODO
 * @Created by Domenic
 */
public class HelloDrpcImpl implements HelloDrpc {

    @Override
    public String sayHello(String msg) {
        return "Hello from, " + msg + "!";
    }

}
