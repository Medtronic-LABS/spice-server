package com.mdtlabs.coreplatform.spiceadminservice;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * RestTemplateConfig class configures a RestTemplate with specified timeouts and a custom error handler.
 * </p>
 */
@Configuration
public class RestTemplateConfig {

    @Value("${app.connectionTimeOut:30000}")
    private Long connectionTimeOut;

    @Value("${app.readTimeOut:30000}")
    private Long readTimeOut;

    /**
     * <p>
     * This method is used to get a RestTemplate object with specified connection and read timeouts and a custom error handler.
     * </p>
     *
     * @param restTemplateResponseErrorHandler {@link RestTemplateResponseErrorHandler} The error handler is used to handle errors that occur during REST API
     *                                         calls made using the RestTemplate is given
     * @return {@link RestTemplate} A rest template that is built using the rest template builder is returned
     */
    @Bean
    public RestTemplate getRestTemplate(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(connectionTimeOut))
                .setReadTimeout(Duration.ofMillis(readTimeOut))
                .errorHandler(restTemplateResponseErrorHandler);
        return restTemplateBuilder.build();
    }
}