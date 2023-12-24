package com.shopping.online.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    @Min(value = 0, message = "Price is not less than 0")
    private Float price;

    @Min(value = 1, message = "Quantity must be great than 0")
    private int quantity;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money not have less than 0")
    private Float totalMoney;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product id must be great than 0")
    private int productId;

    @JsonProperty("order_id")
    @Min(value = 1, message = "Order id must be great than 0")
    private int orderId;
}
