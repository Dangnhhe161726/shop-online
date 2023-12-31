package com.shopping.online.controllers;

import com.shopping.online.dtos.BrandDTO;
import com.shopping.online.exceptions.InvalidParamException;
import com.shopping.online.models.Brand;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.services.brand.IBrandService;
import com.shopping.online.validations.ValidationDataRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/brands")
@RequiredArgsConstructor
public class BrandController {
    private final IBrandService brandService;
    private String timeStamp = LocalDateTime.now().toString();

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllBrand(
    ) {
        List<Brand> brands = brandService.getAllBrand();
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .message("Get all brand success")
                        .data(Map.of("brands", brands))
                        .build()
        );
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> insertBrand(
            @Valid @RequestBody BrandDTO brandDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                throw new InvalidParamException(
                        ValidationDataRequest.getMessageError(result)
                );
            }
            Brand newBrand = brandService.createBrand(brandDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Created brand success")
                            .data(Map.of("new_brand", newBrand))
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
    public ResponseEntity<HttpResponse> updateBrand(
            @PathVariable("id") Long id,
            @RequestBody BrandDTO brandDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                throw new InvalidParamException(
                        ValidationDataRequest.getMessageError(result)
                );
            }
            Brand updateBrand = brandService.updateBrand(id, brandDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("update brand success")
                            .data(Map.of("update_brand", updateBrand))
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
    public ResponseEntity<HttpResponse> deleteBrand(
            @PathVariable("id") Long id
    ) {
        try {
            brandService.deleteBrand(id);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .message("delete brand by id " + id + " success")
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
