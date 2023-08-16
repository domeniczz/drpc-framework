package com.domenic.config;

/**
 * @author Domenic
 * @Classname RegistryConfig
 * @Description TODO
 * @Created by Domenic
 */
public class RegistryConfig {

    /**
     * <p>Connection type & url & port</p>
     * <p>Example: "zookeeper://127.0.0.1:2181"; "zookeeper://" is type, "127.0.0.1" is url, "2181" is port</p>
     */
    private final String connectString;

    private final int timeout;

    public RegistryConfig(String connectString) {
        this.connectString = connectString;
        this.timeout = 5000;
    }

    public RegistryConfig(String connectString, int timeout) {
        this.connectString = connectString;
        this.timeout = timeout;
    }

    public String getConnectString() {
        return connectString;
    }

    public int getTimeout() {
        return timeout;
    }

}
