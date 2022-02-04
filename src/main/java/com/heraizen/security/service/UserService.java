package com.heraizen.security.service;

import com.heraizen.security.domain.User;
import com.heraizen.security.dto.UserDto;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;

public interface UserService {

    String registerUser(UserDto userDto, HttpServletRequest request) throws DataFormatException;

    void sendVerificationEmail(User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException;

    String verifyUser(String token);
}
