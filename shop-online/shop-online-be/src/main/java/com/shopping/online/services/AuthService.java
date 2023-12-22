package com.shopping.online.services;


import com.shopping.online.dtos.LoginDTO;
import com.shopping.online.dtos.RegisterDTO;
import com.shopping.online.models.UserEntity;

public interface AuthService {

    String login (LoginDTO loginDto);
    UserEntity register(RegisterDTO registerDto);
    Boolean verifyToken(String token);
}
