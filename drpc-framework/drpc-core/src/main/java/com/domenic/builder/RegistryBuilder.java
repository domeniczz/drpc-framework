package com.domenic.builder;

import com.domenic.config.RegistryConfig;
import com.domenic.discovery.Registry;
import com.domenic.discovery.impl.NacosRegistry;
import com.domenic.discovery.impl.ZookeeperRegistry;
import com.domenic.exceptions.DiscoveryException;
import com.domenic.utils.registry.RegistryUtils;

/**
 * @author Domenic
 * @Classname RegistryBuilder
 * @Description Registry Builder
 * @Created by Domenic
 */
public class RegistryBuilder {

    public Registry build(RegistryConfig config) {
        String registryType = RegistryUtils.getRegistryType(config.getConnectString());
        String connectionAddr = RegistryUtils.getConnectionAddress(config.getConnectString());
        switch (registryType) {
            case "zookeeper":
                return new ZookeeperRegistry(connectionAddr, config.getTimeout());
            case "nacos":
                return new NacosRegistry(connectionAddr, config.getTimeout());
            default:
                throw new DiscoveryException("Unsupported registry type: " + registryType);
        }
    }

}
