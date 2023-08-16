package com.domenic;

import com.domenic.builder.RegistryBuilder;
import com.domenic.config.ProtocolConfig;
import com.domenic.config.ReferenceConfig;
import com.domenic.config.RegistryConfig;
import com.domenic.config.ServiceConfig;
import com.domenic.discovery.Registry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

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

    // registry center
    private Registry registry;

    /**
     * <p>A list for published services</p>
     * <p>key: interface's fully qualified name; value: ServiceConfig</p>
     */
    private static final Map<String, ServiceConfig<?>> SERVICES = new ConcurrentHashMap<>(16);

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
        this.registryConfig = registryConfig;
        this.registry = new RegistryBuilder().build(registryConfig);
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
            log.debug("The project uses protocol: {}", protocolConfig.toString());
        }
        return this;
    }

    /**
     * Publish a service (register implementation of the service to the registry center)
     * @param service service config
     * @return current instance
     */
    public DrpcBootstrap service(ServiceConfig<?> service) {
        registry.register(service);
        SERVICES.put(service.getInterface().getName(), service);
        return this;
    }

    /**
     * Publish a list of services (register implementations of the services to the registry center)
     * @param services service config
     * @return current instance
     */
    public DrpcBootstrap services(List<ServiceConfig<?>> services) {
        for (ServiceConfig<?> service : services) {
            this.service(service);
        }
        return this;
    }

    /**
     * start the service
     */
    public void start() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    /**
     * Get the reference of the service to be consumed
     * @param reference reference config
     * @return current instance
     */
    public DrpcBootstrap reference(ReferenceConfig<?> reference) {
        reference.setRegistry(registry);
        return this;
    }

}
