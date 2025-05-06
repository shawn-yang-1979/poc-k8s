package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import de.codecentric.boot.admin.server.config.AdminServerProperties;

/**
 * Reference:
 * 
 * https://docs.spring.io/spring-security/reference/5.7/servlet/configuration/java.html#_multiple_httpsecurity
 * 
 * https://docs.spring-boot-admin.com/2.7.14/#_securing_spring_boot_admin_server
 * 
 */
@Configuration(proxyBeanMethods = false)
public class SecuritySecureConfig {
  private final AdminServerProperties adminServer;

  public SecuritySecureConfig(AdminServerProperties adminServer) {
    this.adminServer = adminServer;
  }

  @Bean
  @Order(1)
  SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
    http.antMatcher(this.adminServer.path("/actuator/**"))//
        .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults())//
        .csrf(csrf -> csrf.disable());
    return http.build();
  }

  @Bean
  @Order(2)
  SecurityFilterChain adminServerfilterChain(HttpSecurity http) throws Exception {
    SavedRequestAwareAuthenticationSuccessHandler successHandler =
        new SavedRequestAwareAuthenticationSuccessHandler();
    successHandler.setTargetUrlParameter("redirectTo");
    successHandler.setDefaultTargetUrl(this.adminServer.path("/"));
    http.authorizeHttpRequests(authorizeRequests -> authorizeRequests //
        .antMatchers(this.adminServer.path("/assets/**")).permitAll() // <1>
        .antMatchers(this.adminServer.path("/login")).permitAll()//
        .anyRequest().authenticated()) // <2>
        .formLogin(formLogin -> formLogin//
            .loginPage(this.adminServer.path("/login"))//
            .successHandler(successHandler).and() // <3>
        )//
        .logout(logout -> logout//
            .logoutUrl(this.adminServer.path("/logout")))//
        .csrf(csrf -> csrf//
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
    return http.build();
  }
}
