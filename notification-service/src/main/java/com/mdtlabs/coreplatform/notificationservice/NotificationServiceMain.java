package com.mdtlabs.coreplatform.notificationservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mdtlabs.coreplatform.common.Constants;

/**
 * <p>
 * NotificationServiceMain is the main class for a Spring Boot application that enables scheduling,
 * Feign clients, Eureka client, and Swagger documentation for an admin API.
 * </p>
 *
 * @author Vigneshkumar created on Jun 30, 2022.
 */
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class})
@ComponentScan(value = Constants.PACKAGE_CORE_PLATFORM)
@EnableEurekaClient
@EnableScheduling
@EnableFeignClients
@OpenAPIDefinition(servers = {@Server(url = "${app.swagger-server-url}")},
        info = @Info(title = "Admin API", version = Constants.VERSION, description = Constants.DOCUMENTATION_USER_API))
@EnableSchedulerLock(defaultLockAtMostFor = "${app.shedlock.sms.start}")
public class NotificationServiceMain {

    /**
     * <p>
     * This is the main function that runs the NotificationServiceApplication class in a Spring Boot application.
     * </p>
     *
     * @param args {@link String} Argument array to be passed is given
     */
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceMain.class, args);
    }
}
