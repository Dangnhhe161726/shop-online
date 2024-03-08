package com.shopping.online.controllers;

import com.github.javafaker.Faker;
import com.shopping.online.dtos.ProductDTO;
import com.shopping.online.exceptions.InvalidParamException;
import com.shopping.online.models.Product;
import com.shopping.online.models.ProductImage;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.responses.ProductResponse;
import com.shopping.online.responses.ProductsResponse;
import com.shopping.online.services.product.IProductService;
import com.shopping.online.services.product.image.IProductImageService;
import com.shopping.online.validations.ValidationDataRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final IProductImageService productImageService;

    private String timeStamp = LocalDateTime.now().toString();

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").descending()
        );
        Page<ProductsResponse> products = productService.getAllProduct(pageRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .message("Get all product success")
                        .data(Map.of("index_page", products.getPageable().getPageNumber(),
                                        "products", products,
                                        "total_page", products.getTotalPages()
                                )
                        )
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getProductById(
            @PathVariable("id") Long id
    ) {
        try {
            ProductResponse product = productService.getProductById(id);
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

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
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
                throw new InvalidParamException(
                        ValidationDataRequest.getMessageError(result)
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
            List<ProductImage> productImages = productImageService.createImages(producId, files);
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
                throw new InvalidParamException(
                        ValidationDataRequest.getMessageError(result)
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
                            .timeStamp(timeStamp)
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

    @PostMapping("/generate-fake-products")
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> createFakeProducts() {
        try {
            Faker faker = new Faker();
            for (int i = 0; i <= 500; i++) {
                String productName = faker.commerce().productName();
                if (productService.existByName(productName)) {
                    continue;
                }
                List<Long> fakeList = new ArrayList<>();
                fakeList.add((long) faker.number().numberBetween(2, 10));
                ProductDTO productDTO = ProductDTO.builder()
                        .name(productName)
                        .price((float) faker.number().numberBetween(0, 2000000))
                        .quantity(faker.number().numberBetween(0, 100))
                        .status(true)
                        .shortDescription(faker.lorem().sentence(10))
                        .longDescription(faker.lorem().sentence(50))
                        .brandId((long) faker.number().numberBetween(2, 5))
                        .categoryId((long) faker.number().numberBetween(2, 5))
                        .sizes(fakeList)
                        .colors(fakeList)
                        .userId(1L)
                        .build();
                productService.createProduct(productDTO);
            }
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Fake products success")
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
