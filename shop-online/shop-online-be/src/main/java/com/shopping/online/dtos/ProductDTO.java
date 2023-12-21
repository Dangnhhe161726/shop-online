package com.shopping.online.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopping.online.models.Color;
import com.shopping.online.models.SizeEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Product name is not blank")
    @Size(min = 5, max = 200, message = "Product name must be between 5 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Float price;

    @JsonProperty("short_description")
    private String shortDescription;

    @JsonProperty("long_description")
    private String longDescription;

    @JsonProperty("update_time")
    private Timestamp updateTime;

    @JsonProperty("create_time")
    private Timestamp createTime;

    @JsonProperty("brand_id")
    @Min(value = 1, message = "Brand's ID must be > 0")
    private Long brandId;

    @JsonProperty("category_id")
    @Min(value = 1, message = "Category's ID must be > 0")
    private Long categoryId;

    @JsonProperty("sizes")
    private List<SizeEntity> sizeEntities;

    @JsonProperty("colors")
    private List<Color> colors;

}
