package com.heraizen.security.service;

import com.heraizen.security.auth.AppUserService;
import com.heraizen.security.auth.JwtUtil;
import com.heraizen.security.dto.LoginResponse;
import com.heraizen.security.dto.LoginUser;
import com.heraizen.security.repo.UserRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class LogServiceImpl implements LogService {

    private AppUserService userService;

    private AuthenticationManager authManager;

    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder encoder;

    private UserRepository userRepository;

    private UserDetailsService userDetailsService;

    public LogServiceImpl(AppUserService userService, AuthenticationManager authManager, JwtUtil jwtUtil, BCryptPasswordEncoder encoder, UserRepository userRepository, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public LoginResponse userLogin(LoginUser loginUser) {
        try {
            Assert.notNull(loginUser, "Login details cannot be null");
            UserDetails userDetails = validateUser(loginUser);
            log.info("Fetched user details with email: {}", userDetails.getUsername());
            String token = jwtUtil.generateToken(userDetails);
            log.info ("{}", userDetails.getAuthorities().stream().findFirst().get());
            LoginResponse loginResponse = LoginResponse.builder().token(token).username(userDetails.getUsername()).role(String.valueOf((userDetails.getAuthorities().stream().findFirst().get()))).build();
            return loginResponse;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private UserDetails validateUser(LoginUser loginUser) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginUser.getEmail());
            if (userDetails != null) {
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
                return userDetails;
            }
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("User with details not found or bad credentials");
        }
        throw new BadCredentialsException("User with details not found or bad credentials");
    }
}
