package com.myproject.simpleonlineshop.mapper.impl;

import com.myproject.simpleonlineshop.dto.*;
import com.myproject.simpleonlineshop.mapper.MyModelMapper;
import com.myproject.simpleonlineshop.model.*;
import com.myproject.simpleonlineshop.repository.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class myModelMapperImpl implements MyModelMapper {

    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    public myModelMapperImpl(ImageRepository imageRepository, ModelMapper modelMapper) {

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
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setBrand(product.getBrand());
        productDto.setPrice(product.getPrice());
        productDto.setCategory(toCategoryDto(product.getCategory()));
        productDto.setDescription(product.getDescription());
        productDto.setImagesDtos(product.getImages().stream().map(image -> toImageDto(image)).toList());
        return productDto;
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

    @Override
    public OrderDto toOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getOrderId())
                .items(order.getOrderItems().stream().map(this::toOrderItemDto).toList())
                .build();
    }
    @Override
    public OrderItemsDto toOrderItemDto(OrderItem orderItem){
        return OrderItemsDto.builder()
                .price(orderItem.getUnitPrice())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .productId(orderItem.getOrderItemId())
                .build();
    }

    @Override
    public CreateUserRequest toCreateUserRequest(User user) {
        return CreateUserRequest.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .build();
    }
    @Override
    public UpdateUserRequest toUpdateUserRequest(User user) {
        return UpdateUserRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .orders(user.getOrders().stream().map(this::toOrderDto).toList())
                .cart(toCartDto(user.getCart()))
                .build();
    }
    @Override
    public CartDto toCartDto(Cart cart){
        return CartDto.builder()
                .cartId(cart.getId())
                .items(cart.getCartItems().stream().map(this::toCartItemDto).collect(Collectors.toSet()))
                .build();
    }
    @Override
    public CartItemDto toCartItemDto(CartItem cartItem){
        return CartItemDto.builder()
                .itemId(cartItem.getId())
                .unitPrice(cartItem.getUnitPrice())
                .product(toProductDto( cartItem.getProduct()))
                .quantity(cartItem.getQuantity())

                .build();
    }
}