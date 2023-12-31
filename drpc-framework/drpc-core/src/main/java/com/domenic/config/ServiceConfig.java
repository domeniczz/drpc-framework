package com.domenic.config;

/**
 * @author Domenic
 * @Classname ServiceConfig
 * @Description Service Config
 * @Created by Domenic
 */
public class ServiceConfig<T> {

    private Class<T> interfaceProvider;
    private Object reference;

    public Class<T> getInterface() {
        return interfaceProvider;
    }

    public void setInterface(Class<T> interfaceProvider) {
        this.interfaceProvider = interfaceProvider;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

}
