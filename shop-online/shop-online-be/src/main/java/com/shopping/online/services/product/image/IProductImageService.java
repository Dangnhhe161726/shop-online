package com.shopping.online.services.product.image;

import com.shopping.online.models.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductImageService {
    List<ProductImage> createImages(
            Long productId,
            List<MultipartFile> files) throws Exception;
}
