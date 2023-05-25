package com.mdtlabs.coreplatform.servicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <p>
 * ServiceDiscoveryApplication is a Spring Boot application class that enables
 * service discovery and registration using Eureka server and client.
 * </p>
 *
 * @author Vigneshkumar created on 30, Jun 2022.
 */
@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class ServiceDiscoveryApplication {

    /**
     * <p>
     * This is the main function that runs a Spring Boot application for service discovery.
     * </p>
     *
     * @param args {@link String} Argument array to be passed is given
     */
    public static void main(String[] args) {
        SpringApplication.run(ServiceDiscoveryApplication.class, args);
    }
}
