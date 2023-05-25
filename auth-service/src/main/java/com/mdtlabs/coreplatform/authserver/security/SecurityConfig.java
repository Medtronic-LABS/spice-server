package com.mdtlabs.coreplatform.authserver.security;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * <p>
 * In this class is the entry point for user authentication and authorization.
 * </p>
 *
 * @author Vigneshkumar
 * @since 30 Jun 2022
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccess authenticationSuccess() {
        return new AuthenticationSuccess();
    }

    @Bean
    public AuthenticationFailure authenticationFailure() {
        return new AuthenticationFailure();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider();
    }

    @Bean
    public LogoutSuccess logoutSuccess() {
        return new LogoutSuccess();
    }

    /**
     * <p>
     * This method is used to set up CORS configuration for a Java application.
     * </p>
     *
     * @return {@link CorsConfigurationSource}  cors configuration is given
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(Boolean.TRUE);
        config.addAllowedOriginPattern(Constants.ASTERISK_SYMBOL);
        config.addAllowedHeader(Constants.ASTERISK_SYMBOL);
        config.addAllowedMethod(HttpMethod.HEAD);
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.addAllowedMethod(HttpMethod.PATCH);
        config.addAllowedMethod(HttpMethod.OPTIONS);
        source.registerCorsConfiguration(Constants.ASTERISK_SYMBOL
                + Constants.FORWARD_SLASH + Constants.ASTERISK_SYMBOL, config);
        return source;
    }

    /**
     * <p>
     * This method is used to configure the security filter chain for HTTP requests, allowing
     * certain endpoints to be accessed without authentication and setting up login
     * and logout handlers.
     * </p>
     *
     * @param http {@link HttpSecurity}request is given
     * @return {@link SecurityFilterChain}  config is given
     * @throws Exception exception is given
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                .antMatchers(HttpMethod.GET, "/webjars/swagger-ui/**").permitAll().anyRequest()
                .authenticated().and().formLogin().loginProcessingUrl("/session")
                .usernameParameter(FieldConstants.USERNAME).passwordParameter(FieldConstants.PASSWORD)
                .successHandler(authenticationSuccess()).failureHandler(authenticationFailure()).and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
                .logout().logoutUrl("/logout").deleteCookies("JSESSIONID")
                .invalidateHttpSession(Boolean.TRUE)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .addLogoutHandler(logoutSuccess())
                .and().csrf().disable();
        return http.build();
    }

}
