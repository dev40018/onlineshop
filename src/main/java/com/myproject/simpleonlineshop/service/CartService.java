package com.myproject.simpleonlineshop.service;

import com.myproject.simpleonlineshop.model.Cart;
import com.myproject.simpleonlineshop.model.User;

import java.math.BigDecimal;

public interface CartService {
    Cart getCartById(Long id);
    void clearCartById(Long id);
    BigDecimal getTotalPrice(Long id);


    Cart initilizeCart(User user);

    Cart getCartByUserId(Long userId);
}
