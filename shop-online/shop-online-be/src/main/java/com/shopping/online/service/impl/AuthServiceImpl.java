package com.shopping.online.service.impl;


import com.shopping.online.dto.LoginDto;
import com.shopping.online.dto.RegisterDto;
import com.shopping.online.model.Confirmation;
import com.shopping.online.model.Role;
import com.shopping.online.model.UserEntity;
import com.shopping.online.repository.ConfirmationRepository;
import com.shopping.online.repository.RoleRepository;
import com.shopping.online.repository.UserRepository;
import com.shopping.online.sercurity.jwt.JwtGenerator;
import com.shopping.online.service.AuthService;
import com.shopping.online.service.EmailService;
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
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow();

        if (!user.isStatus()) {
            return null;
        }

        var token = jwtGenerator.generateToken(user.getEmail());

        return token;
    }

    @Override
    public UserEntity register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email already exists");
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
        userEntity.setStatus(false);

        Role roles = roleRepository.findByName(registerDto.getRole().getName()).get();
        userEntity.setRoles(Collections.singletonList(roles));
        userRepository.save(userEntity);

        var token = jwtGenerator.generateToken(userEntity.getEmail());
        Confirmation confirmation = new Confirmation(userEntity, token);
        confirmationRepository.save(confirmation);
        //Send email with token
        emailService.sendHtmlEmailWithEmbeddedFiles(userEntity.getFirstName() + userEntity.getLastName(),
                userEntity.getEmail(), token);
        return userEntity;
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        Optional<UserEntity> userEntity = userRepository.findByEmail(confirmation.getUserEntity().getEmail());
        if (!userEntity.isPresent()) {
            throw new RuntimeException("Email verify not exist");
        }
        userEntity.get().setStatus(true);
        confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }
}
