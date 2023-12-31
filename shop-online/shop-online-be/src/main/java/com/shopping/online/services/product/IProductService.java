package com.shopping.online.services.product;

import com.shopping.online.dtos.ProductDTO;
import com.shopping.online.dtos.ProductImageDTO;
import com.shopping.online.models.Product;
import com.shopping.online.models.ProductImage;
import com.shopping.online.responses.ProductResponse;
import com.shopping.online.responses.ProductsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IProductService {
    String storeFile(MultipartFile file) throws IOException;

    Product createProduct(ProductDTO productDTO) throws Exception;

    ProductResponse getProductById(Long id) throws Exception;

    Page<ProductsResponse> getAllProduct(PageRequest pageRequest);

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProductById(Long id) throws Exception;

    ProductImage createProductImage(
            ProductImageDTO productImageDTO) throws Exception;

    Boolean existByName(String name);
}
