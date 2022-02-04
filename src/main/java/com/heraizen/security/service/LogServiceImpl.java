package com.heraizen.security.service;

import com.heraizen.security.auth.AppUserService;
import com.heraizen.security.auth.JwtUtil;
import com.heraizen.security.domain.LoginResponse;
import com.heraizen.security.domain.LoginUser;
import com.heraizen.security.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.assertions.Assertions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

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

    @Override
    public String userLogin(LoginUser loginUser) {
        UserDetails userDetails = validateUser(loginUser);
        String token = jwtUtil.generateToken(userDetails);
        LoginResponse loginResponse = LoginResponse.builder().token(token).build();
        return loginResponse.getToken();
    }

    private UserDetails validateUser(LoginUser loginUser) {
        try {
            com.heraizen.security.domain.User user = userRepository.findByEmail(loginUser.getPassword());
            UserDetails userDetails = User.withUsername(user.getName()).password(user.getPassword()).roles(user.getRole()).disabled(user.isEnabled()).build();
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword()));
            return userDetails;

        } catch (Exception e) {
            throw new BadCredentialsException("User with details not found or bad credentials");
        }
    }
}
