package com.domenic.discovery;

import com.domenic.config.ServiceConfig;

import java.net.InetSocketAddress;

/**
 * @author Domenic
 * @Classname Registry
 * @Description Registry Center
 * @Created by Domenic
 */
public interface Registry {

    /**
     * Register a service to the registry center
     * @param service service config
     */
    void register(ServiceConfig<?> service);

    /**
     * <p>Lookup available service hosts address from the registry center</p>
     * <p>Return the first host in the list</p>
     * TODO:
     * 1. Use cache and watcher rather than look up available hosts from the registry center every time
     * 2. Judge if a service is available rather than just return the first one in the services list (load balancing)
     * @param serviceName service name
     * @return service's address
     */
    InetSocketAddress lookup(String serviceName);

}
