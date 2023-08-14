package com.domenic;

import com.domenic.discovery.RegistryConfig;
import com.domenic.impl.HelloDrpcImpl;

/**
 * @author Domenic
 * @Classname ProviderApplication
 * @Description TODO
 * @Created by Domenic
 */
public class ProviderApplication {

    public static void main(String[] args) {
        // Get ready the service to be published
        ServiceConfig<HelloDrpc> service = new ServiceConfig<>();
        service.setInterface(HelloDrpc.class);
        service.setReference(new HelloDrpcImpl());

        // Publish & start the service
        DrpcBootstrap.getInstance()
                .application("demo-drpc-provider")
                // connect to the registry center
                .registry(new RegistryConfig(Constants.ZK_ADDRESS))
                .protocol(new ProtocolConfig("drpc"))
                // register (publish) the service
                .service(service)
                .start();
    }

}
