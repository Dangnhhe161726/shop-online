package com.shopping.online.service;


import com.shopping.online.dto.LoginDto;
import com.shopping.online.dto.RegisterDto;
import com.shopping.online.model.UserEntity;

public interface AuthService {

    String login (LoginDto loginDto);
    UserEntity register(RegisterDto registerDto);
    Boolean verifyToken(String token);
}
