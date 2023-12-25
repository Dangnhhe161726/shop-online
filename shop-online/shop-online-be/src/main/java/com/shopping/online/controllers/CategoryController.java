package com.shopping.online.controllers;

import com.shopping.online.dtos.CategoryDTO;
import com.shopping.online.models.Category;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.services.CategoryService;
import com.shopping.online.validations.ValidationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private String timeStamp = LocalDateTime.now().toString();

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllCategory(
    ) {
        List<Category> categoryList = categoryService.getAllCategory();
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .message("get all category success")
                        .data(Map.of("categories", categoryList))
                        .build()
        );
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> insertCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
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
            Category newCategory = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("insert category success")
                            .data(Map.of("new_category", newCategory))
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
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryDTO categoryDTO,
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
            Category updateCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .data(Map.of("update_category", updateCategory))
                            .message("update category success")
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> deleteCategory(
            @PathVariable("id") Long id
    ) {
        try{
            categoryService.deleteCategory(id);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("delete category by id " + id + " success")
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

}
