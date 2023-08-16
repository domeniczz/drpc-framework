package com.domenic.discovery.impl;

import com.domenic.config.ServiceConfig;
import com.domenic.discovery.Registry;

import java.net.InetSocketAddress;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname NacosRegistry
 * @Description Nacos Registry Center
 * @Created by Domenic
 */
@Slf4j
public class NacosRegistry implements Registry {

    public NacosRegistry() {
        // ...
    }

    public NacosRegistry(String connectString, int timeout) {
        // ...
    }

    @Override
    public void register(ServiceConfig<?> service) {
        // ...
        if (log.isDebugEnabled()) {
            log.debug("Registered a service: ", service.getInterface().getName());
        }
    }

    @Override
    public InetSocketAddress lookup(String serviceName) {
        // ...
        return null;
    }

}
