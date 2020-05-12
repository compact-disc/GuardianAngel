package com.guardianangel.security;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 10/25/2019
 * 
 * Used to configure Auth0 and Spring framework.
 * This gathers Auth0 tokens and keys from file.
 * This sets up Spring/Thymeleaf security as well.
 */

import com.auth0.AuthenticationController;
import com.guardianangel.mvc.LogoutController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import java.io.UnsupportedEncodingException;

@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppConfig extends WebSecurityConfigurerAdapter {
    /**
     * This is your auth0 domain (tenant you have created when registering with auth0 - account name)
     */
    @Value(value = "${com.auth0.domain}")
    private String domain;

    /**
     * This is the client id of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientId}")
    private String clientId;

    /**
     * This is the client secret of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientSecret}")
    private String clientSecret;

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutController();
    }

    /**
     * Used for consuming and using the auth0 authentication data
     * 
     * @return the authentication controller with all of the auth0 data
     */
    @Bean
    public AuthenticationController authenticationController() throws UnsupportedEncodingException {
        return AuthenticationController.newBuilder(domain, clientId, clientSecret)
                .build();
    }

    /**
     * Configure the security of the Guardian Angel Application and what to allow when not logged in
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
            .authorizeRequests()
            .antMatchers(
            		"/callback",
            		"/login",
            		"/",
            		"/images/sign-in-with-twitter-gray.png",
            		"/css/footer.css",
            		"/css/header.css",
            		"/css/login.css",
            		"/css/fonts/**"
            		).permitAll()
            .anyRequest().authenticated()
            .and()
            .logout().logoutSuccessHandler(logoutSuccessHandler()).permitAll();
    }

    /**
     * Get the domain variable for auth0
     * 
     * @return the domain variable
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Get the client id variable for auth0
     * 
     * @return the client id variable
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Get the client secret variable for auth0
     * 
     * @return the client secret for auth0
     */
    public String getClientSecret() {
        return clientSecret;
    }
}
