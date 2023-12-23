package com.shopping.online.controllers;

import com.shopping.online.dtos.OrderDetailDTO;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.validations.ValidationDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getOrderDetail(
            @Valid @PathVariable("id") Long orderDetailId
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .message("get order detail by id")
                        .build()
        );
    }

    //Get list order detail of oder
    @GetMapping("/order/{id}")
    public ResponseEntity<HttpResponse> getOrderDetails(
            @Valid @PathVariable("id") Long orderId
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .message("get list order detail of order by order id")
                        .build()
        );
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('SALE', 'USER')")
    public ResponseEntity<HttpResponse> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
    ) {
        try {
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
                            .message("Create order detail success")
                            .data(Map.of("new_order_detail", orderDetailDTO))
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
    @PreAuthorize("hasAnyAuthority('SALE', 'USER')")
    public ResponseEntity<HttpResponse> updateOrderDetail(
            @Valid @PathVariable("id") Long orderDetailId,
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
    ){
        try {
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
                            .message("Update order detail success")
                            .data(Map.of("update_order_detail", orderDetailDTO))
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SALE', 'USER')")
    public ResponseEntity<HttpResponse> deleteOrderDetail(
            @Valid @PathVariable("id") Long orderDetailId
    ){
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .message("Delete order detail success")
                        .build()
        );
    }
}
