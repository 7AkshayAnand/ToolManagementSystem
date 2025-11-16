package com.toolmanagementsystem.demo.services;


import com.toolmanagementsystem.demo.dto.LoginDto;
import com.toolmanagementsystem.demo.dto.LoginResponseDto;
import com.toolmanagementsystem.demo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;


    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
//        In your custom login logic, Spring Security does not automatically store the Authentication in the SecurityContext. You need to do it manually using:

//SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);


        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);

        User user = userService.getUserById(userId);

        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }
}
