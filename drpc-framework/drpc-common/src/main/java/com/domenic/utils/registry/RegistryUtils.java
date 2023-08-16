package com.domenic.utils.registry;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname RegistryUtils
 * @Description Registry Utils
 * @Created by Domenic
 */
@Slf4j
public class RegistryUtils {

    private RegistryUtils() {
    }

    /**
     * Get the connection type
     * @param connectionString connection string
     * @return connection type
     */
    public static String getRegistryType(String connectionString) {
        return split(connectionString, "://")[0].toLowerCase();
    }

    /**
     * Get the connection address host:port
     * @param connectionString connection string
     * @return host:port
     */
    public static String getConnectionAddress(String connectionString) {
        return split(connectionString, "://")[1];
    }

    private static String[] split(String connectionString, String delimiter) {
        String[] res = connectionString.split(delimiter);
        if (res.length != 2) {
            log.error("Invalid connection string: {}", connectionString);
            throw new IllegalArgumentException("Invalid connection string: " + connectionString);
        }
        return res;
    }

}
