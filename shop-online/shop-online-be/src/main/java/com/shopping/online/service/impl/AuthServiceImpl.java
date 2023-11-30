package com.shopping.online.service.impl;


import com.shopping.online.dto.AuthResponseDto;
import com.shopping.online.dto.LoginDto;
import com.shopping.online.dto.RegisterDto;
import com.shopping.online.model.Role;
import com.shopping.online.model.UserEntity;
import com.shopping.online.repository.RoleRespository;
import com.shopping.online.repository.UserRespository;
import com.shopping.online.sercurity.jwt.JwtGenerator;
import com.shopping.online.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRespository userRespository;
    private final RoleRespository roleRespository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    @Override
    public AuthResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userRespository.findByEmail(loginDto.getEmail())
                .orElseThrow();
        var token = jwtGenerator.generateToken(user.getEmail());

        return AuthResponseDto
                .builder()
                .accessToken(token)
                .build();
    }

    @Override
    public AuthResponseDto register(RegisterDto registerDto) {
        Optional<UserEntity> userResponse = userRespository.findByEmail(registerDto.getEmail());
        if (userResponse.isPresent()) {
            return null;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(registerDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userEntity.setFirstName(registerDto.getFirstName());
        userEntity.setLastName(registerDto.getLastName());
        userEntity.setAddress(registerDto.getAddress());
        userEntity.setAvatar(registerDto.getAvatar());
        userEntity.setPhoneNumber(registerDto.getPhoneNumber());
        userEntity.setGender(registerDto.isGender());
        userEntity.setStatus(true);

        Role roles = roleRespository.findByName(registerDto.getRole().getName()).get();

        userEntity.setRoles(Collections.singletonList(roles));
        userRespository.save(userEntity);
        var token = jwtGenerator.generateToken(userEntity.getEmail());

        return AuthResponseDto
                .builder()
                .accessToken(token)
                .build();
    }
}
