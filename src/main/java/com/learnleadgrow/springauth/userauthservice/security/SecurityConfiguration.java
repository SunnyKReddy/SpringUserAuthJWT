package com.learnleadgrow.springauth.userauthservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(cors -> cors.disable());
        http.authorizeHttpRequests(auth ->
                auth
                         .anyRequest().permitAll()
                        //.requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/auth/**").permitAll()
                        //.requestMatchers(HttpMethod.POST, "/role").authenticated()
                        //.requestMatchers(HttpMethod.POST, "/user").authenticated()
                        //.requestMatchers("/user/**").authenticated()
                        //.requestMatchers("/role/**").authenticated()
                );
        return http.build();
    }
}
