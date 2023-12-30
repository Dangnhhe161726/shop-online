package com.shopping.online.services.product.image;

import com.shopping.online.dtos.ProductImageDTO;
import com.shopping.online.exceptions.InvalidParamException;
import com.shopping.online.models.ProductImage;
import com.shopping.online.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductService productService;

    @Override
    public List<ProductImage> createImages(
            Long productId,
            List<MultipartFile> files) throws Exception {
        List<ProductImage> productImages = new ArrayList<>();
        files = files == null ? new ArrayList<MultipartFile>() : files;
        if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException(
                    "image upload maximum is " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT
            );
        }
        if (files.get(0).getOriginalFilename().isEmpty()) {
            throw new InvalidParamException(
                    "File is not null"
            );
        }
        for (MultipartFile file : files) {
            if (file.getSize() == 0) {
                continue;
            }
            //check size of file and format
            if (file.getSize() > 10 * 1024 * 1024) {
                //file greater than 10 MB
                throw new InvalidParamException(
                        "File have size less than 10MB"
                );
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException(
                        "Unsupported media type"
                );
            }
            //save file and update
            String filename = productService.storeFile(file);

            //save in database
            ProductImage newProductImage = productService.createProductImage(
                    ProductImageDTO.builder()
                            .imageUrl(filename)
                            .productId(productId)
                            .build()
            );
            productImages.add(newProductImage);
        }
        return productImages;
    }
}
