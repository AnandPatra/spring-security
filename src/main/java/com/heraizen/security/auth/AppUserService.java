package com.heraizen.security.auth;

import com.heraizen.security.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private Map<String, UserDetails> map = new HashMap<>();

    @Autowired
    public AppUserService(BCryptPasswordEncoder encoder) {
        UserDetails userDetails = User.withUsername("Anand").password("123").passwordEncoder(encoder::encode).roles("USER").build();
        UserDetails userDetails1 = User.withUsername("Admin").password("123").passwordEncoder(encoder::encode).roles("ADMIN").build();
        map.put(userDetails.getUsername(), userDetails);
        map.put(userDetails1.getUsername(), userDetails1);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.heraizen.security.domain.User user = userRepository.findByEmail(email);
        String role = user.getRole();
        return new User(user.getName(), user.getPassword(), user.isEnabled(), true, true, true,  new ArrayList<GrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority(role))));
    }

    public void addUserDetails(UserDetails userDetails, String username) {
        map.put(username, userDetails);
    }
}
