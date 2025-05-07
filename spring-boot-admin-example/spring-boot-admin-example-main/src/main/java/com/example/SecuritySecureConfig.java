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
 * https://docs.spring.io/spring-boot/docs/2.7.18/reference/htmlsingle/#actuator.endpoints.security
 * 
 * https://docs.spring.io/spring-security/reference/5.7/servlet/configuration/java.html#_multiple_httpsecurity
 * 
 * https://docs.spring-boot-admin.com/2.7.14/#_securing_spring_boot_admin_server
 * 
 * https://www.youtube.com/watch?v=PczgM2L3w60
 * 
 */
@Configuration(proxyBeanMethods = false)
public class SecuritySecureConfig {
  private final AdminServerProperties adminServer;

  public SecuritySecureConfig(AdminServerProperties adminServer) {
    this.adminServer = adminServer;
  }

  /**
   * This settings protect /actuator/** from exposing without basic authentication
   * 
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  @Order(1)
  SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
    http.antMatcher(this.adminServer.path("/actuator/**"))//
        .authorizeHttpRequests(authorize -> authorize//
            .antMatchers(this.adminServer.path("/actuator/info")).permitAll()//
            .antMatchers(this.adminServer.path("/actuator/health/**")).permitAll()//
            .anyRequest().authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults())//
        .csrf(csrf -> csrf.disable());
    return http.build();
  }

  /**
   * This settings protect admin servicer's API from exposing without login authentication
   * 
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  @Order(2)
  SecurityFilterChain adminServerFilterChain(HttpSecurity http) throws Exception {
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
