package com.domenic.config;

import com.domenic.discovery.Registry;
import com.domenic.proxy.handler.ConsumerInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @author Domenic
 * @Classname ReferenceConfig
 * @Description Reference Config
 * @Created by Domenic
 */
public class ReferenceConfig<T> {

    private Class<T> serviceInterface;

    private Registry registry;

    /**
     * Get a proxy instance of the service
     * @return proxy instance
     */
    @SuppressWarnings("unchecked")
    public T get() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?>[] classes = new Class[] { serviceInterface };
        ConsumerInvocationHandler invocationHandler = new ConsumerInvocationHandler(serviceInterface, registry);
        // Get a proxy instance by dynamic proxy
        return (T) Proxy.newProxyInstance(classLoader, classes, invocationHandler);
    }

    public void setInterface(Class<T> referenceInterface) {
        this.serviceInterface = referenceInterface;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

}
