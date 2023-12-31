package com.shopping.online.services.color;

import com.shopping.online.dtos.ColorDTO;
import com.shopping.online.models.Color;
import com.shopping.online.models.Product;
import com.shopping.online.repositories.ColorRepository;
import com.shopping.online.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService implements IColorService {
    private final ColorRepository colorRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Color> getAllColor() {
        return colorRepository.findAll();
    }

    @Override
    public Color getColorById(Long id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Color have id " + id + " not found"));
    }

    @Override
    @Transactional
    public Color createColor(ColorDTO colorDTO) {
        if (colorRepository.existsByName(colorDTO.getName())) {
            throw new IllegalStateException("Name color existed");
        }
        Color newColor = Color.builder()
                .name(colorDTO.getName())
                .code(colorDTO.getCode())
                .build();
        return colorRepository.save(newColor);
    }

    @Override
    @Transactional
    public Color updateColor(Long id, ColorDTO colorDTO) {
        Color existingColor = getColorById(id);
        if (colorRepository.existsByName(colorDTO.getName())) {
            throw new IllegalStateException("Color prepare update has name exited");
        }
        existingColor.setName(colorDTO.getName());
        existingColor.setCode(colorDTO.getCode());
        colorRepository.save(existingColor);
        return existingColor;
    }

    @Override
    @Transactional
    public void deleteColor(Long id) {
        if (!colorRepository.existsById(id)) {
            throw new IllegalStateException("Color have id " + id + " not found");
        }
        List<Product> products = productRepository.findProductsByColorId(id);
        if (!products.isEmpty()) {
            throw new RuntimeException("Can not delete color is still associated with Product");
        }
        colorRepository.deleteById(id);
    }
}
