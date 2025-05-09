package com.myproject.simpleonlineshop.mapper.impl;

import com.myproject.simpleonlineshop.dto.CategoryDto;
import com.myproject.simpleonlineshop.dto.ImageDto;
import com.myproject.simpleonlineshop.dto.ProductDto;
import com.myproject.simpleonlineshop.mapper.ProductMapper;
import com.myproject.simpleonlineshop.model.Category;
import com.myproject.simpleonlineshop.model.Image;
import com.myproject.simpleonlineshop.model.Product;
import com.myproject.simpleonlineshop.repository.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductMapperImpl implements ProductMapper {

    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    public ProductMapperImpl(ImageRepository imageRepository, ModelMapper modelMapper) {

        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public List<ProductDto> getConvertToProductDtos(List<Product> products){
        return products.stream().map(product -> toProductDto1(product)).toList();
    }

    @Override
    public ProductDto toProductDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream().map(image -> toImageDto(image)).toList();
        productDto.setImagesDtos(imageDtos);
        return productDto;
    }

    private ProductDto toProductDto1(Product product) {
//        ProductDto productDto = new ProductDto();
//        productDto.setId(product.getId());
//        productDto.setName(product.getName());
//        productDto.setBrand(product.getBrand());
//        productDto.setPrice(product.getPrice());
//        productDto.setCategory(toCategoryDto(product.getCategory()));
//        productDto.setDescription(product.getDescription());
//        productDto.setImagesDtos(product.getImages().stream().map(image -> toImageDto(image)).toList());
        return modelMapper.map(product, ProductDto.class);
    }
    private CategoryDto toCategoryDto(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        return categoryDto;
    }
    @Override
    public ImageDto toImageDto(Image image){
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setDownloadUrl(image.getDownloadUrl());
        imageDto.setFileName(image.getFileName());
        return imageDto;
    }


    @Override
    public Product toProduct(ProductDto productDto) {
        return null;
    }
}
