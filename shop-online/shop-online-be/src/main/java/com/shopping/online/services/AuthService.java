package com.shopping.online.services;


import com.shopping.online.dtos.LoginDto;
import com.shopping.online.dtos.RegisterDto;
import com.shopping.online.models.UserEntity;

public interface AuthService {

    String login (LoginDto loginDto);
    UserEntity register(RegisterDto registerDto);
    Boolean verifyToken(String token);
}
