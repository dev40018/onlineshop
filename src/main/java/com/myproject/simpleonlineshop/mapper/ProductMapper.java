package com.myproject.simpleonlineshop.mapper;

import com.myproject.simpleonlineshop.dto.ImageDto;
import com.myproject.simpleonlineshop.dto.ProductDto;
import com.myproject.simpleonlineshop.model.Image;
import com.myproject.simpleonlineshop.model.Product;

import java.util.List;

public interface ProductMapper {
    List<ProductDto> getConvertToProductDtos(List<Product> products);

    ProductDto toProductDto(Product product);

    ImageDto toImageDto(Image image);

    Product toProduct(ProductDto productDto);
}
