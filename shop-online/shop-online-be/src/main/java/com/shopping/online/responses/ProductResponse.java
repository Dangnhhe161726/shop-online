package com.shopping.online.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopping.online.models.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("thumnail")
    private String thumnail;

    @JsonProperty("des_thumnail")
    private String desThumnail;

    @JsonProperty("short_description")
    private String shortDescription;

    @JsonProperty("long_description")
    private String longDescription;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("category")
    private Category category;

    @JsonProperty("brand")
    private Brand brand;

    @JsonProperty("colors")
    private List<Color> colors;

    @JsonProperty("sizes")
    private List<SizeEntity> sizeEnties;

    @JsonProperty("product_images")
    private List<ProductImage> productImages;
}
