package com.shopping.online.services.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopping.online.responses.ProductsResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductRedisService {
    List<ProductsResponse> getAllProducts(
            PageRequest pageRequest) throws Exception;
}
