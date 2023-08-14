package com.domenic;

/**
 * @author Domenic
 * @Classname Constants
 * @Description common constants
 * @Created by Domenic
 */
public class Constants {

    private Constants() {
    }

    public static final String ZK_HOST = System.getProperty("zookeeper.address", "127.0.0.1");
    public static final String ZK_PORT = System.getProperty("zookeeper.port", "2181");
    public static final String ZK_ADDRESS = /* "zookeeper://" +  */ZK_HOST + ":" + ZK_PORT;

    public static final int DEFAULT_ZK_TIMEOUT = 50000;

    public static final String BASE_PROVIDERS_PATH = "/drpc-metadata/providers";
    public static final String BASE_CONSUMERS_PATH = "/drpc-metadata/consumers";

}
