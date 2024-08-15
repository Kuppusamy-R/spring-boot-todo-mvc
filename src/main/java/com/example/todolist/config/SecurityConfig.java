package com.example.todolist.config;

import com.example.todolist.controller.WebSocketLogoutController;
import com.example.todolist.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, WebSocketLogoutController webSocketLogoutController) throws Exception {

       http.authorizeHttpRequests(authorize -> authorize
                       .requestMatchers("/login", "/logout", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                       .anyRequest().authenticated())
               .formLogin(form -> form
                       .loginPage("/login")
                       .defaultSuccessUrl("/todo", true)
                       .failureUrl("/login?error=true")
                       .permitAll())
               .logout(logout -> logout
                       .logoutUrl("/logout")
                       .logoutSuccessUrl("/login?logout")
                       .invalidateHttpSession(true)
                       .deleteCookies("JSESSIONID")
                       .permitAll())
               .sessionManagement(session -> session
                       .invalidSessionUrl("/login?sessionExpired=true")
                       .maximumSessions(1)
                       .expiredUrl("/login?sessionExpired=true")
                       .sessionRegistry(sessionRegistry()));

       return  http.build();
    }
}
