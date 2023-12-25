package com.shopping.online.repositories;

import com.shopping.online.models.Brand;
import com.shopping.online.models.Category;
import com.shopping.online.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    List<Product> findByBrand(Brand brand);
}
