package com.shopping.online.controllers;

import com.shopping.online.dtos.ProductDTO;
import com.shopping.online.models.ProductImage;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.services.ProductService;
import com.shopping.online.validations.ValidationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllProduct(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("get all product")
                        .data(Map.of("page", page, "limit", limit))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getProductById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("get Product by id")
                        .data(Map.of("id", id))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<HttpResponse> insertProduct(
            @Valid @RequestBody ProductDTO productDTO,
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
                        .data(Map.of("newProduct", productDTO))
                        .message("insert product success")
                        .build()
        );
    }

    @PostMapping(value = "/uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse> uploadImages(
            @PathVariable("id") Long producId,
            @ModelAttribute("files") List<MultipartFile> files
    ) {
        try {
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .message("image upload maximum is 6")
                                .build()
                );
            }

            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                //check size of file and format
                if (file.getSize() > 10 * 1024 * 1024) {
                    //file greater than 10 MB
                    return ResponseEntity.badRequest().body(
                            HttpResponse.builder()
                                    .timeStamp(LocalDateTime.now().toString())
                                    .message("file have size less than 10MB")
                                    .build()
                    );
                }

                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest().body(
                            HttpResponse.builder()
                                    .timeStamp(LocalDateTime.now().toString())
                                    .message("unsupported media type")
                                    .build()
                    );
                }
                //save file and update
                String filename = productService.storeFile(file);
            }
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .message("ok")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .message(e.getMessage())
                            .build()
            );
        }


    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpResponse> updateProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductDTO productDTO,
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
                        .data(Map.of("updateProduct", productDTO, "id", id))
                        .message("update product success")
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteProduct(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("id", id))
                        .message("delete product success")
                        .build()
        );
    }
}
