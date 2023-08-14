package com.domenic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.registry.Registry;

/**
 * @author Domenic
 * @Classname ReferenceConfig
 * @Description TODO
 * @Created by Domenic
 */
public class ReferenceConfig<T> {

    private Class<T> referenceInterface;

    private Registry registry;

    public Class<T> getInterface() {
        return referenceInterface;
    }

    public void setInterface(Class<T> referenceInterface) {
        this.referenceInterface = referenceInterface;
    }

    @SuppressWarnings("unchecked")
    public T get() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?>[] classes = new Class[]{referenceInterface};

        // Get a proxy instance by using dynamic proxy
        return (T) Proxy.newProxyInstance(classLoader, classes, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        });
    }

}
