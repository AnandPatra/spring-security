package com.heraizen.security.web;


import com.heraizen.security.auth.AppUserService;
import com.heraizen.security.auth.JwtUtil;
import com.heraizen.security.domain.LoginResponse;
import com.heraizen.security.domain.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/auth/", produces = "application/json" ,consumes="application/json")
public class LoginController {

    @Autowired
    private AppUserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginUser loginUser) {
        UserDetails userDetails = validateUser(loginUser);
        String token = jwtUtil.generateToken(userDetails);
        LoginResponse loginResponse = LoginResponse.builder().token(token).build();
        return ResponseEntity.ok().body(loginResponse);
    }

    private UserDetails validateUser(LoginUser loginUser) {
        try {
            UserDetails userDetails = userService.loadUserByUsername(loginUser.getUserName());
            if (userDetails != null) {

                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword()));
                return userDetails;
            }

        } catch (Exception e) {
            throw new BadCredentialsException("User with details not found or bad credentials");
        }

        throw new BadCredentialsException("User with details not found or bad credentials");
    }

}
