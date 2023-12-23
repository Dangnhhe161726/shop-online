package com.shopping.online.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @JsonProperty("full_name")
    @NotBlank(message = "Full name is not blank")
    private String fullName;

    @JsonProperty("phone_number")
    @Pattern(regexp = "[0-9]{10}", message = "Phone number it not valid")
    private String phoneNumber;

    @NotBlank(message = "Address is not blank")
    private String address;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money not have less than 0")
    private Float total_money;

    @NotBlank(message = "status is not blank")
    private String status;

    @JsonProperty("shipping_method")
    @NotBlank(message = "Shipping method is not blank")
    private String shippingMethod;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("shipping_address")
    @NotBlank(message = "Shipping address is not blank")
    private String shippingAddress;

    @JsonProperty("payment_method")
    @NotBlank(message = "Payment method is not blank")
    private String paymentMethod;

    @JsonProperty("user_id")
    @Min(value = 1, message = "User id must be great than 0")
    private Long userId;
}
