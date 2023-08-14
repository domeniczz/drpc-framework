package com.domenic;

import com.domenic.discovery.RegistryConfig;
import lombok.extern.slf4j.Slf4j;

import java.rmi.registry.Registry;
import java.util.List;

/**
 * @author Domenic
 * @Classname DrpcBootstrap
 * @Description TODO
 * @Created by Domenic
 */
@Slf4j
public class DrpcBootstrap {

    /**
     * Singleton instance of DrpcBootstrap
     */
    private static final DrpcBootstrap DRPC_BOOTSTRAP = new DrpcBootstrap();

    private String appName = "default";
    private RegistryConfig registryConfig;
    private ProtocolConfig protocolConfig;
    private int port = 8088;
    private Registry registry;

    private DrpcBootstrap() {
    }

    public static DrpcBootstrap getInstance() {
        return DRPC_BOOTSTRAP;
    }

    /**
     * Set the application name
     * @param appName application name
     * @return current instance
     */
    public DrpcBootstrap application(String appName) {
        this.appName = appName;
        return this;
    }

    /**
     * Config a registry center
     * @param registryConfig registry config
     * @return current instance
     */
    public DrpcBootstrap registry(RegistryConfig registryConfig) {
        // this.registry = registryConfig.getRegistry();
        return this;
    }

    /**
     * Config the protocol the service uses
     * @param protocolConfig protocol config
     * @return current instance
     */
    public DrpcBootstrap protocol(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
        if (log.isDebugEnabled()) {
            log.debug("The project uses protocol: ", protocolConfig.toString());
        }
        return this;
    }

    /**
     * Publish a service (register implementation of the service to the registry center)
     * @param service service config
     * @return current instance
     */
    public DrpcBootstrap service(ServiceConfig<?> service) {
        if (log.isDebugEnabled()) {
            log.debug("Published a service: ", protocolConfig.toString());
        }
        return this;
    }

    /**
     * Publish a list of services (register implementations of the services to the registry center)
     * @param services service config
     * @return current instance
     */
    public DrpcBootstrap services(List<ServiceConfig<?>> services) {
        if (log.isDebugEnabled()) {
            log.debug("Published a list of services: ", protocolConfig.toString());
        }
        return this;
    }

    /**
     * start the service
     */
    public void start() {
    }

    /**
     * Get the reference of the service to be consumed
     * @param reference reference config
     * @return current instance
     */
    public DrpcBootstrap reference(ReferenceConfig<?> reference) {
        return this;
    }

}
