package com.shopping.online.controller.auth;

import com.shopping.online.dto.AuthResponseDto;
import com.shopping.online.dto.LoginDto;
import com.shopping.online.dto.RegisterDto;
import com.shopping.online.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        AuthResponseDto authResponseDto = authService.login(loginDto);
        return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        AuthResponseDto authResponseDto = authService.register(registerDto);
        if(authResponseDto == null){
            return new ResponseEntity<>("Email is existed!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
    }
}
