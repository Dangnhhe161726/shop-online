package com.shopping.online.services.brand;

import com.shopping.online.dtos.BrandDTO;
import com.shopping.online.models.Brand;

import java.util.List;

public interface IBrandService {
    List<Brand> getAllBrand();

    Brand getBrandById(Long id);

    Brand createBrand(BrandDTO brandDTO);

    Brand updateBrand(Long id, BrandDTO brandDTO);

    void deleteBrand(Long id) throws Exception;
}
