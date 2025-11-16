package com.toolmanagementsystem.demo.config;


import com.toolmanagementsystem.demo.auth.AuditorAwareImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "getAuditorImpl")
public class AppConfig {
    @Bean
    ModelMapper getModelMapper(){

        return new ModelMapper();
    }
    @Bean
    AuditorAware<String> getAuditorImpl(){

        return new AuditorAwareImpl();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
