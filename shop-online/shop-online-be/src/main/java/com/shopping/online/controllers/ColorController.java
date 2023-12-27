package com.shopping.online.controllers;

import com.shopping.online.dtos.ColorDTO;
import com.shopping.online.models.Color;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.services.color.ColorService;
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
@RequestMapping("${api.prefix}/colors")
@RequiredArgsConstructor
public class ColorController {
    private final ColorService colorService;
    private String timeStamp = LocalDateTime.now().toString();

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllColor() {
        List<Color> colors = colorService.getAllColor();
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .message("Get all color success")
                        .data(Map.of("colors", colors))
                        .build()
        );
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> createColor(
            @Valid @RequestBody ColorDTO colorDTO,
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
            Color newColor = colorService.createColor(colorDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Color created")
                            .data(Map.of("new_color", newColor))
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
    public ResponseEntity<HttpResponse> updateColor(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody ColorDTO colorDTO,
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
            Color updateColor = colorService.updateColor(id, colorDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Color updated")
                            .data(Map.of("update_color", updateColor))
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
    public ResponseEntity<HttpResponse> deleteColor(
            @Valid @PathVariable("id") Long id
    ) {
        try {
            colorService.deleteColor(id);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .message("Color deleted")
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
