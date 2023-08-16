package com.domenic;

import com.domenic.config.ProtocolConfig;
import com.domenic.config.RegistryConfig;
import com.domenic.config.ServiceConfig;
import com.domenic.service.HelloDrpc;
import com.domenic.service.impl.HelloDrpcImpl;

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
                .registry(new RegistryConfig(Constants.DEFAULT_ADDRESS))
                .protocol(new ProtocolConfig("drpc"))
                // register (publish) the service
                .service(service)
                .start();
    }

}
