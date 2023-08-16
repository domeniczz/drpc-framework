package com.domenic.constants;

/**
 * @author Domenic
 * @Classname Constants
 * @Description common constants
 * @Created by Domenic
 */
public class RegistryConstants {

    private RegistryConstants() {
    }

    public static final String PATH_SEPARATOR = "/";

    private static final String TYPE_PREFIX = "zookeeper://";
    private static final String HOST = System.getProperty("zookeeper.address", "127.0.0.1");
    private static final String PORT = System.getProperty("zookeeper.port", "2181");

    public static final String DEFAULT_ADDRESS = TYPE_PREFIX + HOST + ":" + PORT;
    public static final int DEFAULT_TIMEOUT = 50000;

    public static final String BASE_ROOT_PATH = PATH_SEPARATOR + "drpc-metadata";
    public static final String BASE_PROVIDERS_PATH = BASE_ROOT_PATH + PATH_SEPARATOR + "providers";
    public static final String BASE_CONSUMERS_PATH = BASE_ROOT_PATH + PATH_SEPARATOR + "consumers";

    public static final int LOCAL_HOST_PORT = 8088;

}
