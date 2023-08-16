package com.domenic;

import com.domenic.bootstrap.DrpcBootstrap;
import com.domenic.config.ReferenceConfig;
import com.domenic.config.RegistryConfig;
import com.domenic.constants.RegistryConstants;
import com.domenic.service.HelloDrpc;

/**
 * @author Domenic
 * @Classname ConsumerApplication
 * @Description Consumer Application
 * @Created by Domenic
 */
public class ConsumerApplication {

    public static void main(String[] args) {
        // Get the reference of the service to be consumed
        ReferenceConfig<HelloDrpc> reference = new ReferenceConfig<>();
        reference.setInterface(HelloDrpc.class);

        // The reference will be automatically initialized when it's used for the first time
        // 1. Connect to the registry center
        // 2. Get the service list from the registry center
        // 3. Choose a service provider & connect to it
        DrpcBootstrap.getInstance()
                .application("demo-drpc-consumer")
                // connect to the registry center
                .registry(new RegistryConfig(RegistryConstants.DEFAULT_ADDRESS))
                // get a reference to the service
                .reference(reference);

        // 4. Send request (to the service) and will get response
        HelloDrpc helloYrpc = reference.get();
        helloYrpc.sayHello("Hello from Consumer");
    }

}
