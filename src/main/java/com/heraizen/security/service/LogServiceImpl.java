package com.heraizen.security.service;

import com.heraizen.security.auth.AppUserService;
import com.heraizen.security.auth.JwtUtil;
import com.heraizen.security.domain.LoginResponse;
import com.heraizen.security.domain.LoginUser;
import com.heraizen.security.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class LogServiceImpl implements LogService {

    @Autowired
    private AppUserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public String userLogin(LoginUser loginUser) {
        try {
            UserDetails userDetails = validateUser(loginUser);
            String token = jwtUtil.generateToken(userDetails);
            LoginResponse loginResponse = LoginResponse.builder().token(token).build();
            return loginResponse.getToken();
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
