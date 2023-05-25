package com.mdtlabs.coreplatform.spiceadminservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.mdtlabs.coreplatform.common.Constants;

/**
 * <p>
 * This is the main class for an Admin Service application in Java, which excludes certain
 * auto-configurations, scans specific packages, enables Eureka client and Feign clients, and defines
 * OpenAPI documentation.
 * </p>
 *
 * @author Vigneshkumar created on Jun 30, 2022.
 */
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class})
@ComponentScan(value = Constants.PACKAGE_CORE_PLATFORM)
@EnableEurekaClient
@EnableFeignClients
@OpenAPIDefinition(servers = {@Server(url = "${app.swagger-server-url}")},
        info = @Info(title = "Admin API", version = Constants.VERSION, description = Constants.DOCUMENTATION_USER_API))
public class AdminServiceApplication {

    /**
     * <p>
     * This is the main function that runs the AdminServiceApplication class in a Spring Boot application.
     * </p>
     *
     * @param args {@link String} Argument array to be passed is given
     */
    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }
}
