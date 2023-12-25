package com.shopping.online.controllers;

import com.shopping.online.dtos.SizeDTO;
import com.shopping.online.models.SizeEntity;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.services.SizeService;
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
@RequestMapping("${api.prefix}/sizes")
@RequiredArgsConstructor
public class SizeController {
    private final SizeService sizeService;
    private String timeStamp = LocalDateTime.now().toString();

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllSize() {
        List<SizeEntity> sizeEntities = sizeService.getAllSize();
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .message("Get all size success")
                        .data(Map.of("sizes", sizeEntities))
                        .build()
        );
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> createSize(
            @Valid @RequestBody SizeDTO sizeDTO,
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
            SizeEntity newSizeEntity = sizeService.createSize(sizeDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Size created")
                            .data(Map.of("new_size", newSizeEntity))
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
    public ResponseEntity<HttpResponse> updateSize(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody SizeDTO sizeDTO,
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
            SizeEntity updateSizeEntity = sizeService.updateSize(id, sizeDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Size updated")
                            .data(Map.of("update_size", updateSizeEntity))
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
    public ResponseEntity<HttpResponse> deleteSize(
            @Valid @PathVariable("id") Long id
    ) {
        try {
            sizeService.deleteSize(id);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Size deleted")
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
