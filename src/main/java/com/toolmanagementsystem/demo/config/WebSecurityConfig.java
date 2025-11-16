package com.toolmanagementsystem.demo.config;


import com.toolmanagementsystem.demo.JwtAuthFilter;
import com.toolmanagementsystem.demo.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {


    private final JwtAuthFilter jwtAuthFilter;


    private static final String[] publicRoutes = {
             "/auth/**", "/home.html"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(publicRoutes).permitAll()



                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}


//// Allow all GET for posts
////                        .requestMatchers(HttpMethod.GET, "/tool/**").permitAll()
//
//// Only ADMIN can create posts
//                        .requestMatchers(HttpMethod.POST, "/tool/**")
//                        .hasRole(Role.ADMIN.name())
//
//        // Only ADMIN can delete posts
//        .requestMatchers(HttpMethod.DELETE, "/tool/**")
//                        .hasRole(Role.ADMIN.name())