package com.mdtlabs.coreplatform.authserver;

import com.mdtlabs.coreplatform.common.Constants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * <p>
 * This is the main class for an authentication server application in Java, which enables various
 * features such as discovery client, Swagger documentation, JPA repositories, caching, and more.
 * </p>
 *
 * @author Vigneshkumar
 * @since Jun 30, 2022
 */
@EnableDiscoveryClient
@EnableSwagger2WebMvc
@ComponentScan(value = Constants.PACKAGE_CORE_PLATFORM)
@EnableJpaRepositories(value = Constants.PACKAGE_CORE_PLATFORM)
@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class})
@OpenAPIDefinition(servers = {@Server(url = "${app.swagger-server-url}")},
        info = @Info(title = Constants.AUTH_API, version = Constants.VERSION,
                description = Constants.DOCUMENTATION_AUTH_API))
@EnableCaching
public class AuthServerApplication {

    /**
     * <p>
     * This is the main function that runs the AuthApplication class in a Spring Boot application.
     * </p>
     *
     * @param args {@link String} Argument array to be passed is given
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
