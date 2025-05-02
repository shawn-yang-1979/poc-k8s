package com.example;

import java.util.UUID;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import de.codecentric.boot.admin.server.config.AdminServerProperties;

@Configuration(proxyBeanMethods = false)
public class SecuritySecureConfig {
  private final AdminServerProperties adminServer;
  private final SecurityProperties security;

  public SecuritySecureConfig(AdminServerProperties adminServer, SecurityProperties security) {
    this.adminServer = adminServer;
    this.security = security;
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
        .formLogin((formLogin) -> formLogin.loginPage(this.adminServer.path("/login"))
            .successHandler(successHandler).and() // <3>
        ).logout((logout) -> logout.logoutUrl(this.adminServer.path("/logout")))
        .httpBasic(Customizer.withDefaults()) // <4>
        .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // <5>
            .ignoringRequestMatchers(
                new AntPathRequestMatcher(this.adminServer.path("/instances"),
                    HttpMethod.POST.toString()), // <6>
                new AntPathRequestMatcher(this.adminServer.path("/instances/*"),
                    HttpMethod.DELETE.toString()), // <6>
                new AntPathRequestMatcher(this.adminServer.path("/actuator/**")) // <7>
            )).rememberMe(rememberMe -> rememberMe.key(UUID.randomUUID().toString())
                .tokenValiditySeconds(1209600));
    return http.build();
  }

  // Required to provide UserDetailsService for "remember functionality"
  @Bean
  InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails admin = User.withUsername(security.getUser().getName())
        .password(passwordEncoder.encode(security.getUser().getPassword())).roles("USER").build();
    return new InMemoryUserDetailsManager(admin);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


}
