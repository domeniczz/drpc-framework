package com.domenic.utils.zookeeper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Domenic
 * @Classname ZookeeperNode
 * @Description Zookeeper node metadata
 * @Created by Domenic
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZookeeperNode {

    private String nodePath;
    private byte[] data;

}
