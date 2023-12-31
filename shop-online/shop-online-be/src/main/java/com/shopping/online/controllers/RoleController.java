package com.shopping.online.controllers;

import com.shopping.online.dtos.RoleDTO;
import com.shopping.online.exceptions.InvalidParamException;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.validations.ValidationDataRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
    private String timeStamp = LocalDateTime.now().toString();
    @GetMapping("")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<HttpResponse> getAllRole() {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .message("Get all role success")
                        .data(Map.of("roles", null))
                        .build()
        );
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<HttpResponse> createRole(
            @Valid @RequestBody RoleDTO roleDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                throw new InvalidParamException(
                        ValidationDataRequest.getMessageError(result)
                );
            }
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Create role success")
                            .data(Map.of("new_role", null))
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<HttpResponse> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody RoleDTO roleDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                throw new InvalidParamException(
                        ValidationDataRequest.getMessageError(result)
                );
            }
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .data(Map.of("update_role", null))
                            .message("Update role success")
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
