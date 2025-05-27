package com.myproject.simpleonlineshop.dto;

import com.myproject.simpleonlineshop.model.Cart;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
