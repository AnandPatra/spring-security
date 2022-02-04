package com.heraizen.security.web;


import com.heraizen.security.auth.AppUserService;
import com.heraizen.security.auth.JwtUtil;
import com.heraizen.security.domain.LoginResponse;
import com.heraizen.security.domain.LoginUser;
import com.heraizen.security.dto.UserDto;
import com.heraizen.security.service.LogService;
import com.heraizen.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.zip.DataFormatException;


@RestController
@RequestMapping(value = "/api/auth/")
public class LoginController {

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginUser loginUser) {

        return ResponseEntity.ok().body(logService.userLogin(loginUser));
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto, HttpServletRequest request) throws DataFormatException {
        return  ResponseEntity.ok().body(userService.registerUser(userDto, request));
    }

    @GetMapping("verify")
    public ResponseEntity<String> verifyUser(@RequestParam("code") String token) {
        return ResponseEntity.ok().body(userService.verifyUser(token));
    }

}
