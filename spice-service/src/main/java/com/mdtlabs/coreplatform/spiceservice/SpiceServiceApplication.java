package com.mdtlabs.coreplatform.spiceservice;

import com.mdtlabs.coreplatform.common.Constants;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * This is the main class for an Spice Service application in Java, which enables Eureka client, Feign clients,
 * and Swagger documentation, and also sets up a scheduler lock.
 * </p>
 *
 * @author Vigneshkumar created on Jun 30, 2022.
 */
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class})
@ComponentScan(value = Constants.PACKAGE_CORE_PLATFORM)
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
@OpenAPIDefinition(servers = {@Server(url = "${app.swagger-server-url}")}, info = @Info(title = "Spice API", version = Constants.VERSION,
        description = Constants.DOCUMENTATION_USER_API))
@EnableSchedulerLock(defaultLockAtMostFor = "${app.shedlock.outbound.start}")
public class SpiceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiceServiceApplication.class, args);
    }
}
