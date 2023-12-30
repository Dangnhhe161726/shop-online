package com.shopping.online.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private Float price;

    @Column(length = 225)
    private String thumnail;

    @Column(name = "short_description", length = 255)
    @JsonProperty("short_description")
    private String shortDescription;

    @Column(name = "long_description", length = 1000)
    @JsonProperty("long_description")
    private String longDescription;

    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private LocalDateTime updateTime;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private LocalDateTime createTime;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean status;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "product_color",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "color_id", referencedColumnName = "id"))
    private List<Color> colors = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "product_size",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "size_id", referencedColumnName = "id"))
    @JsonProperty("sizes")
    private List<SizeEntity> sizeEnties = new ArrayList<>();

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonProperty("product_images")
    private List<ProductImage> productImages;

}
