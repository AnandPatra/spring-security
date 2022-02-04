package com.heraizen.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AppUserService implements UserDetailsService {

    private Map<String, UserDetails> map = new HashMap<>();

    @Autowired
    public AppUserService(BCryptPasswordEncoder encoder) {
        UserDetails userDetails = User.withUsername("Anand").password("123").passwordEncoder(encoder::encode).roles("USER").build();
        UserDetails userDetails1 = User.withUsername("Admin").password("123").passwordEncoder(encoder::encode).roles("ADMIN").build();
        map.put(userDetails.getUsername(), userDetails);
        map.put(userDetails1.getUsername(), userDetails1);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return map.get(username);
    }

    public void addUserDetails(UserDetails userDetails, String username) {
        map.put(username, userDetails);
    }
}
