package com.shopping.online.controllers;

import com.shopping.online.dtos.ProductDTO;
import com.shopping.online.dtos.ProductImageDTO;
import com.shopping.online.exceptions.InvalidParamException;
import com.shopping.online.models.Product;
import com.shopping.online.models.ProductImage;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.services.product.ProductService;
import com.shopping.online.validations.ValidationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private String timeStamp = LocalDateTime.now().toString();

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllProduct(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .message("get all product")
                        .data(Map.of("page", page, "limit", limit))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getProductById(
            @PathVariable("id") Long id
    ) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Get product by id " + id)
                            .data(Map.of("product", product))
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

    @PostMapping("")
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
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
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Insert product success")
                            .data(Map.of("new_product", newProduct))
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

    @PostMapping(value = "/uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> uploadImages(
            @PathVariable("id") Long producId,
            @ModelAttribute("files") List<MultipartFile> files
    ) {
        try {
            List<ProductImage> productImages = new ArrayList<>();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                throw new InvalidParamException(
                        "image upload maximum is " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT
                );
            }

            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                //check size of file and format
                if (file.getSize() > 10 * 1024 * 1024) {
                    //file greater than 10 MB
                    throw new InvalidParamException(
                            "File have size less than 10MB"
                    );
                }

                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new IllegalArgumentException(
                            "Unsupported media type"
                    );
                }
                //save file and update
                String filename = productService.storeFile(file);

                //save in database
                ProductImage newProductImage = productService.createProductImage(
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .productId(producId)
                                .build()
                );
                productImages.add(newProductImage);
            }
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Product have id " + producId + " has been created image")
                            .data(Map.of("product_images", productImages))
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
    public ResponseEntity<HttpResponse> updateProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductDTO productDTO,
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
            Product updateProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("update product success")
                            .data(Map.of("update_product", updateProduct))
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
    public ResponseEntity<HttpResponse> deleteProduct(
            @PathVariable("id") Long id
    ) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .message("Delete product success")
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
