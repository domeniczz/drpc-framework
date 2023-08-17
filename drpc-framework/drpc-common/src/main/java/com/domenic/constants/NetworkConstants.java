package com.domenic.constants;

public class NetworkConstants {

    private NetworkConstants() {
    }

    public static final String HOST = System.getProperty("netty.address", "0.0.0.0");
    public static final int PORT = Integer.parseInt(System.getProperty("netty.port", "8088"));

}
