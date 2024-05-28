package com.mdtlabs.coreplatform.spiceservice;

import com.mdtlabs.coreplatform.AuthenticationFilter;
import com.mdtlabs.coreplatform.common.Constants;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

/**
 * <p>
 * SecurityConfiguration class configures security settings for an API, including CORS configuration, authorization
 * rules, and OpenAPI documentation.
 * </p>
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * <p>
     * This method is used to configure the security filter chain for HTTP requests, including CORS configuration,
     * authorization rules, authentication filters, and session management.
     * </P>
     *
     * @param http                 {@link HttpSecurity} The `http` is used to configure security settings for
     *                             the application's HTTP requests. It is used to define the security filter chain that
     *                             will be applied to incoming requests is given
     * @param authenticationFilter {@link AuthenticationFilter} The authenticationFilter is a custom filter that
     *                             handles authentication for incoming requests is given
     * @return {@link SecurityFilterChain} The SecurityFilterChain is being returned
     * @throws Exception The exception in CorsConfiguration is thrown
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
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable().httpBasic();  //NOSONAR
        return http.build();
    }

    /**
     * <p>
     * This method is used to create a custom OpenAPI specification for a Spice Service API with security schemes for
     * tenant ID and authorization.
     * <p>
     *
     * @return {@link OpenAPI} The custom OpenAPI is being returned.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Spice Service API").version("1.0.0"))
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

    /**
     * <p>
     * This method is used to create a lock provider using a JdbcTemplate object as the data source.
     * <p>
     *
     * @param dataSource {@link JdbcTemplate} The dataSource is a Spring Framework class that provides a convenient way
     *                   to interact with a relational database using JDBC (Java Database Connectivity). It is used
     *                   to execute SQL statements and manage database connections is given
     * @return {@link LockProvider} the `LockProvider` is being returned
     */
    @Bean
    public LockProvider lockProvider(JdbcTemplate dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }
}
