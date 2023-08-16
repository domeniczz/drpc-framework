package com.domenic;

import com.domenic.constants.RegistryConstants;
import com.domenic.utils.registry.impl.zookeeper.ZookeeperNode;
import com.domenic.utils.registry.impl.zookeeper.ZookeeperUtils;

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
        initRegistryCenter();
    }

    /**
     * <p>Initialize Registry Center (create basic structure)</p>
     * <p>
     * Nodes tructure:<br/>
     * /<br/>
     *   ∟ /drpc-metadata<br/>
     *       ∟ /providers<br/>
     *       ∟ /consumers<br/>
     * </p>
     */
    private static void initRegistryCenter() {
        // establish Zookeeper connection
        ZooKeeper zk = ZookeeperUtils.connect();

        // define node's metadata
        ZookeeperNode baseNode = new ZookeeperNode(RegistryConstants.BASE_ROOT_PATH, null);
        ZookeeperNode providersNode = new ZookeeperNode(RegistryConstants.BASE_PROVIDERS_PATH, null);
        ZookeeperNode consumersNode = new ZookeeperNode(RegistryConstants.BASE_CONSUMERS_PATH, null);

        // create basic nodes
        List.of(baseNode, providersNode, consumersNode)
                .forEach(node -> ZookeeperUtils.createNode(zk, node, null, CreateMode.PERSISTENT));

        // close Zookeeper connection
        ZookeeperUtils.close(zk);
    }

}
