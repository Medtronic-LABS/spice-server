package com.mdtlabs.coreplatform.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>
 * This is the main class for a Spring Boot application that enables discovery client for a gateway.
 * </p>
 *
 * @author Vigneshkumar created on Jun 30, 2022.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    /**
     * <p>
     * This is the main function that runs the GatewayApplication class in a Spring Boot application.
     * </p>
     *
     * @param args {@link String} Argument array to be passed is given
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
