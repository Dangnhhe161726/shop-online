package com.shopping.online.services.impl;


import com.shopping.online.dtos.LoginDto;
import com.shopping.online.dtos.RegisterDto;
import com.shopping.online.models.Confirmation;
import com.shopping.online.models.Role;
import com.shopping.online.models.UserEntity;
import com.shopping.online.repositories.ConfirmationRepository;
import com.shopping.online.repositories.RoleRepository;
import com.shopping.online.repositories.UserRepository;
import com.shopping.online.sercurities.jwt.JwtGenerator;
import com.shopping.online.services.AuthService;
import com.shopping.online.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        userEntity.setDob(registerDto.getDob());
        userEntity.setStatus(false);

        List<Role> roles = new ArrayList<>();
        for (Role role: registerDto.getRoles()) {
            roles.add(roleRepository.findByName(role.getName()).get());
        }

        userEntity.setRoles(roles);
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
