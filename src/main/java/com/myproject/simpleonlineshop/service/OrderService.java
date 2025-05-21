package com.myproject.simpleonlineshop.service;

import com.myproject.simpleonlineshop.model.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);
    Order getOrder(Long orderId);

    List<Order> getUsersOrder(Long userId);
}
