package com.shopping.online.service;


import com.shopping.online.dto.AuthResponseDto;
import com.shopping.online.dto.LoginDto;
import com.shopping.online.dto.RegisterDto;

public interface AuthService {

    AuthResponseDto login (LoginDto loginDto);
    AuthResponseDto register(RegisterDto registerDto);
}
