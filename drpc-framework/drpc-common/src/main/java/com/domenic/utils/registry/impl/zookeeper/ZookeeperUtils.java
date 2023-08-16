package com.domenic.utils.registry.impl.zookeeper;

import com.domenic.constants.RegistryConstants;
import com.domenic.exceptions.ZookeeperException;
import com.domenic.utils.registry.RegistryUtils;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname ZookeeperUtils
 * @Description Zookeeper Utils
 * @Created by Domenic
 */
@Slf4j
public class ZookeeperUtils {

    private ZookeeperUtils() {
    }

    /**
     * Create a Zookeeper connection with default args
     * @return ZooKeeper connection instance
     */
    public static ZooKeeper connect() {
        // connection string
        String connectString = RegistryUtils.getConnectionAddress(RegistryConstants.DEFAULT_ADDRESS);
        // zookeeper timeout
        int timeout = RegistryConstants.DEFAULT_TIMEOUT;
        return connect(connectString, timeout);
    }

    /**
     * Create a Zookeeper connection with provided args
     * @param connectString connection string
     * @param timeout zookeeper timeout
     * @return ZooKeeper connection instance
     */
    public static ZooKeeper connect(String connectString, int timeout) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            // try to connect to Zookeeper server
            final ZooKeeper zooKeeper = new ZooKeeper(connectString, timeout, event -> {
                // go into clause when the connection is established
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    if (log.isDebugEnabled()) {
                        log.debug("Zookeeper connection established! (address: {}, timeout: {})", connectString, timeout);
                    }
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
            return zooKeeper;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Establishing Zookeeper connection was interrupted: {}", e);
            throw new ZookeeperException(e);
        } catch (IOException e) {
            log.error("Exception while creating Zookeeper connection: {}", e);
            throw new ZookeeperException(e);
        }
    }

    /**
     * Close Zookeeper connection
     * @param zk ZooKeeper connection
     */
    public static void close(ZooKeeper zk) {
        try {
            if (zk != null) {
                zk.close();
            } else {
                log.error("Zookeeper connection is null! Cannot close it!");
                throw new ZookeeperException("Zookeeper connection is null! Cannot close it!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Close Zookeeper connection was interrupted: {}", e);
            throw new ZookeeperException(e);
        }
    }

    /**
     * Create a Zookeeper node
     * @param zk ZooKeeper connection
     * @param node node metadata
     * @param watcher watcher
     * @param createMode create mode
     * @return true if the node was created, false otherwise
     */
    public static Boolean createNode(ZooKeeper zk, ZookeeperNode node, Watcher watcher, CreateMode createMode) {
        try {
            if (zk == null) {
                log.error("Zookeeper connection is null! Cannot create node!");
                throw new ZookeeperException("Zookeeper connection is null! Cannot create node!");
            }
            if (zk.exists(node.getNodePath(), watcher) == null) {
                String result = zk.create(node.getNodePath(), node.getData(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                log.info("Node created: {}", result);
                return true;
            } else {
                if (log.isDebugEnabled()) {
                    log.info("Node already exists, node path: {}", node.getNodePath());
                }
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Creating Zookeeper node was interrupted: {}", e);
            throw new ZookeeperException(e);
        } catch (KeeperException e) {
            log.error("Exception while creating Zookeeper node: {}", e);
            throw new ZookeeperException(e);
        }
    }

    /**
     * Check if a Zookeeper node exists
     * @param zk ZooKeeper connection
     * @param node node path
     * @param watcher watcher
     * @return true if the node exists, false otherwise
     */
    public static boolean exists(ZooKeeper zk, String node, Watcher watcher) {
        try {
            if (zk != null) {
                return zk.exists(node, watcher) != null;
            } else {
                log.error("Zookeeper connection is null! Cannot check node existance!");
                throw new ZookeeperException("Zookeeper connection is null! Cannot check node existance!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Checking Zookeeper node existance was interrupted: {}", e);
            throw new ZookeeperException(e);
        } catch (KeeperException e) {
            log.error("Exception while checking Zookeeper node ({}) existance: {}", node, e);
            throw new ZookeeperException(e);
        }
    }

    /**
     * Get a Zookeeper node's children
     * @param zk ZooKeeper connection
     * @param serviceNode service node
     * @param watcher watcher
     * @return a list of children nodes
     */
    public static List<String> getChildren(ZooKeeper zk, String serviceNode, Watcher watcher) {
        try {
            if (zk != null) {
                return zk.getChildren(serviceNode, watcher);
            } else {
                log.error("Zookeeper connection is null! Cannot get node children!");
                throw new ZookeeperException("Zookeeper connection is null! Cannot get node children!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Getting Zookeeper node children was interrupted: {}", e);
            throw new ZookeeperException(e);
        } catch (KeeperException e) {
            log.error("Exception while getting Zookeeper node (service node: {}) children: {}", serviceNode, e);
            throw new ZookeeperException(e);
        }
    }

}
