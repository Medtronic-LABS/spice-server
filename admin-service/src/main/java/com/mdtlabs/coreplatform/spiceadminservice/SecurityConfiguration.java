package com.mdtlabs.coreplatform.spiceadminservice;

import java.util.Arrays;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.mdtlabs.coreplatform.AuthenticationFilter;
import com.mdtlabs.coreplatform.common.Constants;

/**
 * <p>
 * SecurityConfiguration class that configures security settings for an application, including
 * authentication and authorization mechanisms, and creates a custom OpenAPI specification
 * for admin service API with security schemes and requirements.
 * </p>
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * <p>
     * This method is used to configure the security filter chain for HTTP requests, allowing certain methods
     * and endpoints to be accessed without authentication while requiring authentication for all
     * other requests.
     * </p>
     *
     * @param http                 {@link HttpSecurity} The http security is used to configure security settings for
     *                             the application, and allows you to specify which requests should be authenticated,
     *                             which authentication mechanisms to use, and how to handle exceptions
     *                             related to authentication and authorization is given
     * @param authenticationFilter {@link SecurityFilterChain} The authenticationFilter is a custom filter
     *                             that handles authentication for incoming requests, and it is added to the
     *                             filter chain before the UsernamePasswordAuthenticationFilter, which is
     *                             the default filter for handling basic authentication is given
     * @return {@link SecurityFilterChain} The SecurityFilterChain that is built using http is returned
     * @throws Exception The exception in cors is thrown
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationFilter authenticationFilter) throws Exception {
        http.cors().configurationSource(request -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowedMethods(Arrays.asList(HttpMethod.DELETE.name(), HttpMethod.GET.name(),
                            HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name()));
                    cors.applyPermitDefaultValues();
                    cors.addAllowedOrigin(Constants.ASTERISK_SYMBOL); //NOSONAR
                    cors.addAllowedOriginPattern(Constants.ASTERISK_SYMBOL);
                    return cors;
                }).and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                .antMatchers(HttpMethod.GET, "/webjars/swagger-ui/**").permitAll().anyRequest()
                .authenticated().and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable().httpBasic(); //NOSONAR
        return http.build();
    }

    /**
     * <p>
     * This method is used to create a custom OpenAPI specification for a user service API with
     * security schemes and requirements.
     * </p>
     *
     * @return {@link OpenAPI} A custom OpenAPI object is being returned
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Admin Service API").version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes(Constants.TENANT_COLUMN_NAME,
                                new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                                        .name(Constants.HEADER_TENANT_ID))
                        .addSecuritySchemes(Constants.AUTHORIZATION_HEADER,
                                new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                                        .name(Constants.AUTHORIZATION)))
                .addSecurityItem(new SecurityRequirement().addList(Constants.AUTHORIZATION_HEADER,
                        Constants.TENANT_COLUMN_NAME));
    }
}
