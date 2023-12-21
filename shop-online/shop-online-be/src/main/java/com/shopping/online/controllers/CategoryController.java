package com.shopping.online.controllers;

import com.shopping.online.dtos.CategoryDTO;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllCategory(
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("get all category")
                        .build()
        );
    }


    @PostMapping("")
    public ResponseEntity<HttpResponse> insertCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
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
                        .data(Map.of("newCategory", categoryDTO))
                        .message("insert category success")
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpResponse> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryDTO categoryDTO,
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
                        .data(Map.of("id", id, "updateCategory", categoryDTO))
                        .message("update category success")
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteCategory(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("id", id))
                        .message("delete category by id success")
                        .build()
        );
    }

}
