package com.domenic;

/**
 * @author Domenic
 * @Classname ServiceConfig
 * @Description TODO
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
