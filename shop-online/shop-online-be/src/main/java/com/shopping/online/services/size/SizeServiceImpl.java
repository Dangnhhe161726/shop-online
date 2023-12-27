package com.shopping.online.services.size;

import com.shopping.online.dtos.SizeDTO;
import com.shopping.online.models.Product;
import com.shopping.online.models.SizeEntity;
import com.shopping.online.repositories.ProductRepository;
import com.shopping.online.repositories.SizeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;
    private final ProductRepository productRepository;

    @Override
    public List<SizeEntity> getAllSize() {
        return sizeRepository.findAll();
    }

    @Override
    public SizeEntity getSizeById(Long id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Size have id " + id + " not found"));
    }

    @Override
    @Transactional
    public SizeEntity createSize(SizeDTO sizeDTO) {
        if (sizeRepository.existsByName(sizeDTO.getName())) {
            throw new IllegalStateException("Size existed");
        }
        SizeEntity newSizeEntity = SizeEntity.builder()
                .name(sizeDTO.getName())
                .build();
        return sizeRepository.save(newSizeEntity);
    }

    @Override
    @Transactional
    public SizeEntity updateSize(Long id, SizeDTO sizeDTO) {
        SizeEntity existingSizeEntity = getSizeById(id);
        if (sizeRepository.existsByName(sizeDTO.getName())) {
            throw new IllegalStateException("Size existed");
        }
        existingSizeEntity.setName(sizeDTO.getName());
        sizeRepository.save(existingSizeEntity);
        return existingSizeEntity;
    }

    @Override
    @Transactional
    public void deleteSize(Long id) throws Exception {
        if (!sizeRepository.existsById(id)) {
            throw new IllegalStateException("Size have id " + id + " not found");
        }
        List<Product> products = productRepository.findProductsBySizeId(id);
        if (!products.isEmpty()) {
            throw new RuntimeException("Can not delete size is still associated with Product");
        }
        sizeRepository.deleteById(id);
    }
}
