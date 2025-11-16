package com.toolmanagementsystem.demo.entity;


import com.toolmanagementsystem.demo.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    @ElementCollection(fetch= FetchType.EAGER)
//    above when we set fetchtype.eager it means when we fetch the User all the roles associated with user also get fetched
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // If you want only roles, no permissions
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }
}
