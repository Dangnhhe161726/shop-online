package com.shopping.online.services.user;


import com.shopping.online.dtos.LoginDTO;
import com.shopping.online.dtos.RegisterDTO;
import com.shopping.online.exceptions.DataNotFoundException;
import com.shopping.online.models.Confirmation;
import com.shopping.online.models.Role;
import com.shopping.online.models.UserEntity;
import com.shopping.online.repositories.ConfirmationRepository;
import com.shopping.online.repositories.RoleRepository;
import com.shopping.online.repositories.UserRepository;
import com.shopping.online.sercurities.jwt.JwtGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final ConfirmationRepository confirmationRepository;
    private final IEmailService emailService;
    private final ModelMapper modelMapper;

    @Override
    public String login(LoginDTO loginDto) {
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
    @Transactional
    public UserEntity register(RegisterDTO registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        List<Role> roles = new ArrayList<>();
        Optional<Role> checkRole = null;
        for (Long roleId : registerDto.getRoles()) {
            checkRole = roleRepository.findById(roleId);
            if (checkRole.isPresent()) {
                roles.add(checkRole.get());
            } else {
                throw new NoSuchElementException("Not found this role");
            }
        }
        UserEntity userEntity = modelMapper.map(registerDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userEntity.setStatus(false);
        userEntity.setRoles(roles);
        userRepository.save(userEntity);

        var token = jwtGenerator.generateToken(userEntity.getEmail());
        Confirmation confirmation = new Confirmation(userEntity, token);
        confirmationRepository.save(confirmation);

        //Send email with token
        emailService.sendHtmlEmailWithEmbeddedFiles(userEntity.getFirstName() + " " + userEntity.getLastName(),
                userEntity.getEmail(), token);
        return userEntity;
    }

    @Override
    @Transactional
    public Boolean verifyToken(String token) throws Exception {
        Confirmation confirmation = confirmationRepository.findByToken(token)
                .orElseThrow(() -> new DataNotFoundException("Confiamtion not found token"));
        Optional<UserEntity> userEntity = userRepository.findByEmail(
                confirmation.getUserEntity()
                        .getEmail()
        );
        if (!userEntity.isPresent()) {
            throw new RuntimeException("Email verify not exist");
        }
        userEntity.get().setStatus(true);
        confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }
}
