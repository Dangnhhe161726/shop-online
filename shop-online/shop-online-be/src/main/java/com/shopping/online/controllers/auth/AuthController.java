package com.shopping.online.controllers.auth;

import com.shopping.online.responses.HttpResponse;
import com.shopping.online.dtos.LoginDTO;
import com.shopping.online.dtos.RegisterDTO;
import com.shopping.online.models.UserEntity;
import com.shopping.online.responses.UserResponse;
import com.shopping.online.services.user.AuthService;
import com.shopping.online.validations.ValidationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;


@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private String timeStamp = LocalDateTime.now().toString();

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(
            @Valid @RequestBody LoginDTO loginDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message(ValidationDTO.getMessageError(result))
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }

        String token = authService.login(loginDto);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Login successfull")
                        .data(Map.of("Token", token))
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(
            @Valid @RequestBody RegisterDTO registerDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(
                        HttpResponse.builder()
                                .timeStamp(timeStamp)
                                .message(ValidationDTO.getMessageError(result))
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
            }

            if (!registerDto.getPassword().equalsIgnoreCase(registerDto.getRepassword())) {
                return ResponseEntity.badRequest().body(
                        HttpResponse.builder()
                                .timeStamp(timeStamp)
                                .message("New password not equal with repassword")
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
            }

            UserEntity newUserEntity = authService.register(registerDto);
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .message("User created")
                            .data(Map.of("user", modelMapper.map(newUserEntity, UserResponse.class)))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message(e.getMessage())
                            .build()
            );
        }

    }

    @GetMapping
    public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam("token") String token) {
        try {
            Boolean isSuccess = authService.verifyToken(token);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("Verify success")
                            .data(Map.of("Success", isSuccess))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

}
