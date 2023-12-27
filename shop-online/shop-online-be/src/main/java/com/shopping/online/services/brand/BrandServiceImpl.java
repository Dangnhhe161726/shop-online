package com.shopping.online.services.brand;

import com.shopping.online.dtos.BrandDTO;
import com.shopping.online.models.Brand;
import com.shopping.online.models.Product;
import com.shopping.online.repositories.BrandRepository;
import com.shopping.online.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Brand is not found by id " + id));
    }

    @Override
    @Transactional
    public Brand createBrand(BrandDTO brandDTO) {
        if (brandRepository.existsByName(brandDTO.getName())) {
            throw new IllegalStateException("Brand existed");
        }
        Brand newBrand = Brand.builder()
                .name(brandDTO.getName())
                .build();
        return brandRepository.save(newBrand);
    }

    @Override
    @Transactional
    public Brand updateBrand(Long id, BrandDTO brandDTO) {
        Brand existingBrand = getBrandById(id);
        if (brandRepository.existsByName(brandDTO.getName())) {
            throw new IllegalStateException("Brand prepare update has existed");
        }
        existingBrand.setName(brandDTO.getName());
        brandRepository.save(existingBrand);
        return existingBrand;
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) throws Exception {
        Brand existingBrand = getBrandById(id);
        List<Product> products = productRepository.findByBrand(existingBrand);
        if (!products.isEmpty()) {
            throw new IllegalStateException("Cannot delete brand with associated products");
        } else {
            brandRepository.deleteById(id);
        }
    }
}
