package com.shopping.online.services.color;

import com.shopping.online.dtos.ColorDTO;
import com.shopping.online.models.Color;

import java.util.List;

public interface ColorService {
    List<Color> getAllColor();

    Color getColorById(Long id);

    Color createColor(ColorDTO colorDTO);

    Color updateColor(Long id, ColorDTO colorDTO);

    void deleteColor(Long id);

}
