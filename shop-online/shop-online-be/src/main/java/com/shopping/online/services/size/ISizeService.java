package com.shopping.online.services.size;

import com.shopping.online.dtos.SizeDTO;
import com.shopping.online.models.SizeEntity;

import java.util.List;

public interface ISizeService {
    List<SizeEntity> getAllSize();

    SizeEntity getSizeById(Long id);

    SizeEntity createSize(SizeDTO sizeDTO);

    SizeEntity updateSize(Long id, SizeDTO sizeDTO);

    void deleteSize(Long id) throws Exception;
}
