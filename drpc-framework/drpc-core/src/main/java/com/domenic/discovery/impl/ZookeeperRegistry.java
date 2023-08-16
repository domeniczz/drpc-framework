package com.domenic.discovery.impl;

import com.domenic.config.ServiceConfig;
import com.domenic.constants.RegistryConstants;
import com.domenic.discovery.Registry;
import com.domenic.exceptions.DiscoveryException;
import com.domenic.utils.network.NetUtils;
import com.domenic.utils.registry.impl.zookeeper.ZookeeperNode;
import com.domenic.utils.registry.impl.zookeeper.ZookeeperUtils;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import java.net.InetSocketAddress;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname ZookeeperRegistry
 * @Description Zookeeper Registry Center
 * @Created by Domenic
 */
@Slf4j
public class ZookeeperRegistry implements Registry {

    private ZooKeeper zk;

    public ZookeeperRegistry() {
        this.zk = ZookeeperUtils.connect();
    }

    public ZookeeperRegistry(String connectString, int timeout) {
        this.zk = ZookeeperUtils.connect(connectString, timeout);
    }

    @Override
    public void register(ServiceConfig<?> service) {
        // register service node to the registry center
        String serviceNode = RegistryConstants.BASE_PROVIDERS_PATH + RegistryConstants.PATH_SEPARATOR + service.getInterface().getName();
        if (!ZookeeperUtils.exists(zk, serviceNode, null)) {
            ZookeeperNode node = new ZookeeperNode(serviceNode, null);
            ZookeeperUtils.createNode(zk, node, null, CreateMode.PERSISTENT);
        }

        // register the host to the registry center
        String hostNode = serviceNode + RegistryConstants.PATH_SEPARATOR + NetUtils.getIp() + ":" + RegistryConstants.LOCAL_HOST_PORT;
        if (!ZookeeperUtils.exists(zk, hostNode, null)) {
            ZookeeperNode node = new ZookeeperNode(hostNode, null);
            ZookeeperUtils.createNode(zk, node, null, CreateMode.EPHEMERAL);
        }

        if (log.isDebugEnabled()) {
            log.debug("Registered a service: {}", service.getInterface().getName());
        }
    }

    @Override
    public InetSocketAddress lookup(String serviceName) {
        // concat the service node path
        String serviceNode = RegistryConstants.BASE_PROVIDERS_PATH + RegistryConstants.PATH_SEPARATOR + serviceName;

        // get its the children nodes (represents the hosts that provides the service)
        List<String> hostNode = ZookeeperUtils.getChildren(zk, serviceNode, null);
        // get all available hosts
        List<InetSocketAddress> inetSocketAddresses = hostNode.stream().map(ipString -> {
            String[] ipAndPort = ipString.split(":");
            String ip = ipAndPort[0];
            int port = Integer.parseInt(ipAndPort[1]);
            return new InetSocketAddress(ip, port);
        }).toList();

        if (inetSocketAddresses.isEmpty()) {
            throw new DiscoveryException("No available hosts found for service: {}" + serviceName);
        }

        // return the first host in the list
        return inetSocketAddresses.get(0);
    }

}
