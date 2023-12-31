package com.shopping.online.services.product;

import com.shopping.online.responses.ProductsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRedisService implements IProductRedisService{
    @Override
    public List<ProductsResponse> getAllProducts(PageRequest pageRequest) throws Exception {
        return null;
    }
}
