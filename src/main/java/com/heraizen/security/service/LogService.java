package com.heraizen.security.service;

import com.heraizen.security.dto.LoginResponse;
import com.heraizen.security.dto.LoginUser;

public interface LogService {

    LoginResponse userLogin(LoginUser loginUser);

//    String registerUser(LoginUser loginUser) throws DataFormatException;

}
