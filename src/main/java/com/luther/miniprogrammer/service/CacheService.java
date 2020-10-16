package com.luther.miniprogrammer.service;

import com.luther.miniprogrammer.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CacheService {
    private static final List<ProductDto> PRODUCT_LIST = new ArrayList<>();

    public void updateCache(List<ProductDto> dtos) {
        PRODUCT_LIST.clear();
        PRODUCT_LIST.addAll(dtos);
    }

    public List<ProductDto> getCache() {
        return PRODUCT_LIST;
    }
}
