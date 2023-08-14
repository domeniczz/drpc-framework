package com.domenic;

import com.domenic.utils.zookeeper.ZookeeperNode;
import com.domenic.utils.zookeeper.ZookeeperUtils;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * @author Domenic
 * @Classname ManagerApplication
 * @Description Manager of Registry Center
 * @Created by Domenic
 */
public class ManagerApplication {

    public static void main(String[] args) {
        initZk();
    }

    /**
     * Initialize Zookeeper (create basic nodes)
     */
    private static void initZk() {
        // establish Zookeeper connection
        ZooKeeper zooKeeper = ZookeeperUtils.connect();

        // define node's metadata
        String basePath = "/drpc-metadata";
        String providerPath = basePath + "/providers";
        String consumersPath = basePath + "/consumers";
        ZookeeperNode baseNode = new ZookeeperNode(basePath, null);
        ZookeeperNode providersNode = new ZookeeperNode(providerPath, null);
        ZookeeperNode consumersNode = new ZookeeperNode(consumersPath, null);

        // create basic nodes
        List.of(baseNode, providersNode, consumersNode)
                .forEach(node -> ZookeeperUtils.createNode(zooKeeper, node, null, CreateMode.PERSISTENT));

        // close Zookeeper connection
        ZookeeperUtils.close(zooKeeper);
    }

}
