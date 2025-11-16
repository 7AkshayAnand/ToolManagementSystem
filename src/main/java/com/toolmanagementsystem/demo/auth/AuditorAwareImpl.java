package com.toolmanagementsystem.demo.auth;




import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.util.Optional;



public class AuditorAwareImpl implements AuditorAware<String> {

//    @Override
//    public Optional<String> getCurrentAuditor() {
//        System.out.println(" AuditorAware CALLED");
//        return Optional.of("Akshay Anand");
//    }

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("default user");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return Optional.of(((UserDetails) principal).getUsername());
        } else if (principal instanceof String) {
            return Optional.of((String) principal);
        }

        return Optional.of("default user");
    }
}
