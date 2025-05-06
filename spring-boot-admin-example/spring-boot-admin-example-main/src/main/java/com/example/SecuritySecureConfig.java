package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import de.codecentric.boot.admin.server.config.AdminServerProperties;

@Configuration(proxyBeanMethods = false)
public class SecuritySecureConfig {
  private final AdminServerProperties adminServer;

  public SecuritySecureConfig(AdminServerProperties adminServer) {
    this.adminServer = adminServer;
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    SavedRequestAwareAuthenticationSuccessHandler successHandler =
        new SavedRequestAwareAuthenticationSuccessHandler();
    successHandler.setTargetUrlParameter("redirectTo");
    successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

    http.authorizeHttpRequests(authorizeRequests -> authorizeRequests //
        .antMatchers(this.adminServer.path("/assets/**")).permitAll() // <1>
        .antMatchers(this.adminServer.path("/actuator/info")).permitAll()//
        .antMatchers(this.adminServer.path("/actuator/health")).permitAll()//
        .antMatchers(this.adminServer.path("/login")).permitAll()//
        .anyRequest().authenticated()) // <2>
        .formLogin(formLogin -> formLogin//
            .loginPage(this.adminServer.path("/login"))//
            .successHandler(successHandler).and() // <3>
        )//
        .logout(logout -> logout//
            .logoutUrl(this.adminServer.path("/logout")))//
        .csrf(csrf -> csrf//
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // <5>
            .ignoringRequestMatchers(
                new AntPathRequestMatcher(this.adminServer.path("/actuator/**")) // <7>
            ));
    return http.build();
  }
}
