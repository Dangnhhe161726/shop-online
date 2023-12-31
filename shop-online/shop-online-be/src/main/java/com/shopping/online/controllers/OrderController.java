package com.shopping.online.controllers;

import com.shopping.online.dtos.OrderDTO;
import com.shopping.online.exceptions.InvalidParamException;
import com.shopping.online.responses.HttpResponse;
import com.shopping.online.validations.ValidationDataRequest;
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
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    @GetMapping("")
    public ResponseEntity<HttpResponse> getAllOrder(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .message("get all order")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getOrder(
            @Valid @PathVariable("id") Long orderId
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .message("get order by id")
                        .build()
        );
    }

    @GetMapping("user/{user_id}")
    public ResponseEntity<HttpResponse> getAllByUserId(
            @Valid @PathVariable("user_id") Long userId
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .message("get all order by user id")
                        .build()
        );
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('SALE', 'USER')")
    public ResponseEntity<HttpResponse> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                throw new InvalidParamException(
                        ValidationDataRequest.getMessageError(result)
                );
            }

            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .message("Create order success")
                            .data(Map.of("newOrder", orderDTO))
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
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> updateOrder(
            @Valid @PathVariable("id") Long orderId,
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                throw new InvalidParamException(
                        ValidationDataRequest.getMessageError(result)
                );
            }

            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .data(Map.of("update_order", orderDTO))
                            .message("Update order success")
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
    @PreAuthorize("hasAuthority('SALE')")
    public ResponseEntity<HttpResponse> deleteOrder(
            @Valid @PathVariable("id") Long orderId
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .message("Delete order success")
                        .build()
        );
    }

}
