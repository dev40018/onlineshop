package com.myproject.simpleonlineshop.mapper.impl;

import com.myproject.simpleonlineshop.dto.ImageDto;
import com.myproject.simpleonlineshop.dto.ProductDto;
import com.myproject.simpleonlineshop.mapper.ProductMapper;
import com.myproject.simpleonlineshop.model.Image;
import com.myproject.simpleonlineshop.model.Product;
import com.myproject.simpleonlineshop.repository.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductMapperImpl implements ProductMapper {
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    public ProductMapperImpl(ModelMapper modelMapper, ImageRepository imageRepository) {
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
    }

    @Override
    public ProductDto toProductDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProudctId(product.getId());
        List<ImageDto> imageDtos = images.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }

    @Override
    public Product toProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        return product;
    }
}
