package com.myproject.simpleonlineshop.service;

import com.myproject.simpleonlineshop.dto.OrderDto;
import com.myproject.simpleonlineshop.model.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUsersOrder(Long userId);
}
