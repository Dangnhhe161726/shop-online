package com.shopping.online.controllers;

import com.shopping.online.dtos.BrandDTO;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.validations.ValidationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllBrand(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("get all brand")
                        .data(Map.of("page", page, "limit", limit))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getBrandById(
            @PathVariable Long id,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("get brand by id")
                        .data(Map.of("id", id, "page", page, "limit", limit))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<HttpResponse> insertBrand(
            @Valid @RequestBody BrandDTO brandDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .message(ValidationDTO.getMessageError(result))
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("newBrand", brandDTO))
                        .message("insert brand success")
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpResponse> updateBrand(
            @PathVariable Long id,
            @RequestBody BrandDTO brandDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .message(ValidationDTO.getMessageError(result))
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("id", id, "updateBrand", brandDTO))
                        .message("update brand success")
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteBrand(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("id", id))
                        .message("delete brand by id success")
                        .build()
        );
    }

}
