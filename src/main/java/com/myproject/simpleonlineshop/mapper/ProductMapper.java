package com.myproject.simpleonlineshop.mapper;

import com.myproject.simpleonlineshop.dto.ProductDto;
import com.myproject.simpleonlineshop.model.Product;

public interface ProductMapper {
    ProductDto toProductDto(Product product);
    Product toProduct(ProductDto productDto);
}
