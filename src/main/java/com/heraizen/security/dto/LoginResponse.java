package com.heraizen.security.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private String role;
}
