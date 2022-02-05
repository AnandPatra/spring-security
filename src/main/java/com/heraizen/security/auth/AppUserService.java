package com.heraizen.security.auth;

import com.heraizen.security.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class AppUserService implements UserDetailsService {


    private UserRepository userRepository;

    public AppUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.heraizen.security.domain.User user = userRepository.findByEmail(email);
        String role = user.getRole();
        return new User(user.getName(), user.getPassword(), user.isEnabled(), true, true, true, new ArrayList<GrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority(role))));
    }

}
