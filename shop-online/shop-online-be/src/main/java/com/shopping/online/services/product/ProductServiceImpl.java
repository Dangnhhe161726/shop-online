package com.shopping.online.services.product;

import com.shopping.online.dtos.ProductDTO;
import com.shopping.online.dtos.ProductImageDTO;
import com.shopping.online.exceptions.DataNotFoundException;
import com.shopping.online.exceptions.InvalidParamException;
import com.shopping.online.models.*;
import com.shopping.online.repositories.*;
import com.shopping.online.responses.ProductResponse;
import com.shopping.online.services.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;
    public static final String UPLOADS_FOLDER = "uploads";

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // update name file with UUID help to file name is unique
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        //Get path to folder save file
        Path uploadDir = Paths.get(UPLOADS_FOLDER);
        //Check path to folder save file have exist if not exist will create new folder
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        //get full path to folder save image + name image
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        //save file to folder
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        //Get user create product
        Optional<UserEntity> userCreate = Optional.ofNullable(
                userRepository.findById(productDTO.getUserId())
                        .orElseThrow(() -> new DataNotFoundException(
                                        "Can not found user with id" + productDTO.getUserId()
                                )
                        )
        );
        //Check size has exist in database
        List<SizeEntity> sizeEntities = new ArrayList<>();
        Optional<SizeEntity> checkSize = null;
        for (Long sizeId :
                productDTO.getSizes()) {
            checkSize = sizeRepository.findById(sizeId);
            if (checkSize.isPresent()) {
                sizeEntities.add(checkSize.get());
            } else {
                throw new DataNotFoundException(
                        "Can not found size with id" + sizeId
                );
            }
        }
        //Check color has exist in database
        List<Color> colors = new ArrayList<>();
        Optional<Color> checkColor = null;
        for (Long colorId :
                productDTO.getColors()) {
            checkColor = colorRepository.findById(colorId);
            if (checkColor.isPresent()) {
                colors.add(checkColor.get());
            } else {
                throw new DataNotFoundException(
                        "Can not found color with id" + colorId
                );
            }
        }
        //Get category of product
        Optional<Category> category = categoryRepository.findById(
                productDTO.getCategoryId()
        );
        //Get brand of product
        Optional<Brand> brand = brandRepository.findById(
                productDTO.getBrandId()
        );
        //Mapping productDTO -> product
        Product newProduct = modelMapper.map(productDTO, Product.class);
        newProduct.setBrand(brand.get());
        newProduct.setCategory(category.get());
        newProduct.setCreateTime(LocalDateTime.now());
        newProduct.setUserEntity(userCreate.get());
        newProduct.setColors(colors);
        newProduct.setSizeEnties(sizeEntities);
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        Optional<Product> product = Optional.ofNullable(
                productRepository.getDetailProduct(id)
                        .orElseThrow(() -> new DataNotFoundException(
                                        "Can not found product by id" + id
                                )
                        )
        );
        return product.get();
    }

    @Override
    public Page<ProductResponse> getAllProduct(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(
                product -> modelMapper.map(product, ProductResponse.class)
        );
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        //Get user create product
        Optional<UserEntity> userUpdate = Optional.ofNullable(
                userRepository.findById(productDTO.getUserId())
                        .orElseThrow(() -> new DataNotFoundException(
                                        "Can not found user with id" + productDTO.getUserId()
                                )
                        )
        );
        //Check size has exist in database
        List<SizeEntity> sizeEntities = new ArrayList<>();
        Optional<SizeEntity> checkSize = null;
        for (Long sizeId :
                productDTO.getSizes()) {
            checkSize = sizeRepository.findById(sizeId);
            if (checkSize.isPresent()) {
                sizeEntities.add(checkSize.get());
            } else {
                throw new DataNotFoundException(
                        "Can not found size with id" + sizeId
                );
            }
        }
        //Check color has exist in database
        List<Color> colors = new ArrayList<>();
        Optional<Color> checkColor = null;
        for (Long colorId :
                productDTO.getColors()) {
            checkColor = colorRepository.findById(colorId);
            if (checkColor.isPresent()) {
                colors.add(checkColor.get());
            } else {
                throw new DataNotFoundException(
                        "Can not found color with id" + colorId
                );
            }
        }
        //Get category of product
        Optional<Category> category = categoryRepository.findById(
                productDTO.getCategoryId()
        );
        //Get brand of product
        Optional<Brand> brand = brandRepository.findById(
                productDTO.getBrandId()
        );

        Product existingProduct = getProductById(id);
        modelMapper.map(productDTO, existingProduct);

        //set another property
        existingProduct.setUpdateTime(LocalDateTime.now());
        existingProduct.setUserEntity(userUpdate.get());
        existingProduct.setCategory(category.get());
        existingProduct.setBrand(brand.get());
        existingProduct.setSizeEnties(sizeEntities);
        existingProduct.setColors(colors);

        productRepository.save(existingProduct);
        return existingProduct;
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) throws Exception {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new DataNotFoundException(
                    "Can not found product have id " + id
            );
        }
    }

    @Override
    public ProductImage createProductImage(
            ProductImageDTO productImageDTO
    ) throws Exception {
        Product existingProduct = getProductById(
                productImageDTO.getProductId()
        );
        ProductImage newProductImage = ProductImage.builder()
                .imageUrl(productImageDTO.getImageUrl())
                .product(existingProduct)
                .build();
        int size = productImageRepository.findByProductId(
                productImageDTO.getProductId()
        ).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException(
                    "Number image of product can not greater than "
                            + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT
            );
        }
        if (existingProduct.getThumnail() == null) {
            existingProduct.setThumnail(productImageDTO.getImageUrl());
        }
        productRepository.save(existingProduct);
        return productImageRepository.save(newProductImage);
    }

    @Override
    public Boolean existByName(String name) {
        return productRepository.existsByName(name);
    }
}
