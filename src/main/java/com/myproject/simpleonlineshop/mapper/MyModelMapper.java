package com.myproject.simpleonlineshop.mapper;

import com.myproject.simpleonlineshop.dto.*;
import com.myproject.simpleonlineshop.model.*;

import java.util.List;

public interface MyModelMapper {
    List<ProductDto> getConvertToProductDtos(List<Product> products);

    ProductDto toProductDto(Product product);

    ImageDto toImageDto(Image image);

    Product toProduct(ProductDto productDto);

    OrderDto toOrderDto(Order order);

    OrderItemsDto toOrderItemDto(OrderItem orderItem);

    CreateUserRequest toCreateUserRequest(User user);

    UpdateUserRequest toUpdateUserRequest(User user);

    UserDto toUserDto(User user);

    CartDto toCartDto(Cart cart);

    CartItemDto toCartItemDto(CartItem cartItem);
}
