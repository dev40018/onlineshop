package com.myproject.simpleonlineshop.service;

import com.myproject.simpleonlineshop.model.Order;

public interface OrderService {
    Order placeOrder(Long userId);
    Order getOrder(Long orderId);
}
