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
@RequestMapping("${api.prefix}/brands")
@RequiredArgsConstructor
public class BrandController {

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllBrand(
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("get all brand")
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
            @PathVariable("id") Long id,
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
            @PathVariable("id") Long id
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
