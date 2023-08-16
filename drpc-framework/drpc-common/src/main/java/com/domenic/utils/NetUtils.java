package com.domenic.utils;

import com.domenic.exceptions.NetworkException;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname NetUtils
 * @Description TODO
 * @Created by Domenic
 */
@Slf4j
public class NetUtils {

    private NetUtils() {
    }

    public static String getIp() {
        try {
            // Get info of all network interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filter out non-loopback / virtual interfaces / non-up interfaces
                if (iface.isLoopback() || iface.isVirtual() || !iface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // filter out ipv6 address and loopback address
                    if (addr instanceof Inet6Address || addr.isLoopbackAddress()) {
                        continue;
                    }
                    String ipAddress = addr.getHostAddress();
                    if (log.isDebugEnabled()) {
                        log.debug("LAN IP Address: {}", ipAddress);
                    }
                    return ipAddress;
                }
            }
            throw new NetworkException();
        } catch (SocketException e) {
            log.error("Exception while getting LAN IP Address: {}", e);
            throw new NetworkException(e);
        }
    }

}
