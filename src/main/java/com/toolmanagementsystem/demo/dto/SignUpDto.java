package com.toolmanagementsystem.demo.dto;


import com.toolmanagementsystem.demo.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {
    private String email;
    private String password;
    private String name;
    private Set<Role> roles;

}
