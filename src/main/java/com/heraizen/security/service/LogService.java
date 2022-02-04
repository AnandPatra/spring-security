package com.heraizen.security.service;

import com.heraizen.security.domain.LoginUser;

import java.util.zip.DataFormatException;

public interface LogService {

    String userLogin(LoginUser loginUser);

//    String registerUser(LoginUser loginUser) throws DataFormatException;

}
