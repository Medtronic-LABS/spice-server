package com.mdtlabs.coreplatform.userservice;

import com.mdtlabs.coreplatform.common.Constants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * This is the main class for a Spring Boot application that runs a user service with Eureka client and Feign clients
 * enabled.
 * </p>
 *
 * @author Vigneshkumar created on Jun 30, 2022.
 */
@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class})
@ComponentScan(value = Constants.PACKAGE_CORE_PLATFORM)
@EnableEurekaClient
@EnableFeignClients
@OpenAPIDefinition(servers = {
        @Server(url = "${app.swagger-server-url}")}, info = @Info(title = Constants.USER_API,
        version = Constants.VERSION, description = Constants.DOCUMENTATION_USER_API))
public class UserServiceMain {

    /**
     * <p>
     * This is the main function that runs the UserServiceMain class in a Spring Boot application.
     * </p>
     *
     * @param args {@link String} Argument array to be passed is given
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceMain.class, args);
    }
}
