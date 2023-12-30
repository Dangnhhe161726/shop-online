package com.shopping.online.repositories;

import com.shopping.online.models.Brand;
import com.shopping.online.models.Category;
import com.shopping.online.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);

    List<Product> findByCategory(Category category);

    List<Product> findByBrand(Brand brand);

    @Query("SELECT p FROM Product p JOIN p.sizeEnties s WHERE s.id = :size_id")
    List<Product> findProductsBySizeId(@Param("size_id") Long sizeId);

    @Query("SELECT p FROM Product p JOIN p.colors c WHERE c.id = :color_id")
    List<Product> findProductsByColorId(@Param("color_id") Long colorId);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImages WHERE p.id = :product_id")
    Optional<Product> getDetailProduct(@Param("product_id") Long productId);
}
