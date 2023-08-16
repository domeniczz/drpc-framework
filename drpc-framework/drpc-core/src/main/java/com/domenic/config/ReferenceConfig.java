package com.domenic.config;

import com.domenic.discovery.Registry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname ReferenceConfig
 * @Description Reference Config
 * @Created by Domenic
 */
@Slf4j
public class ReferenceConfig<T> {

    private Class<T> serviceInterface;

    private Registry registry;

    @SuppressWarnings("unchecked")
    /**
     * Get a proxy instance of the service
     * @return
     */
    public T get() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?>[] classes = new Class[] { serviceInterface };

        // Get a proxy instance by dynamic proxy
        return (T) Proxy.newProxyInstance(classLoader, classes, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                InetSocketAddress serviceAddr = registry.lookup(serviceInterface.getName());
                if (log.isDebugEnabled()) {
                    log.debug("Discover a service \"{}\" with host address \"{}\" ", serviceInterface.getName(), serviceAddr.toString());
                }
                return null;
            }
        });
    }

    public void setInterface(Class<T> referenceInterface) {
        this.serviceInterface = referenceInterface;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

}
