package com.queueless.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager
    userDetailsService() {

        UserDetails admin =
                User.builder()

                        .username("admin")

                        .password("{noop}admin123")

                        .roles("ADMIN")

                        .build();

        UserDetails operator =
                User.builder()

                        .username("operator")

                        .password("{noop}operator123")

                        .roles("OPERATOR")

                        .build();

        return new InMemoryUserDetailsManager(
                admin,
                operator
        );
    }

    @Bean
    public SecurityFilterChain
    securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/track/**",
                                "/join-queue",
                                "/register"
                        )

                        .permitAll()

                        .requestMatchers(
                                "/dashboard",
                                "/call-next",
                                "/start-service/**",
                                "/complete-service/**",
                                "/miss-ticket/**"
                        )

                        .hasAnyRole(
                                "ADMIN",
                                "OPERATOR"
                        )

                        .requestMatchers(
                                "/open-session",
                                "/close-session"
                        )

                        .hasRole("ADMIN")

                        .anyRequest()

                        .authenticated()
                )

                .formLogin(form -> form

                        .defaultSuccessUrl(
                                "/dashboard",
                                true
                        )

                        .permitAll()
                )

                .logout(logout -> logout
                        .permitAll())

                .exceptionHandling(exception -> exception

                        .accessDeniedPage(
                                "/access-denied"
                        )
                );

        return http.build();
    }
}