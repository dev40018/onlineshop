package com.myproject.simpleonlineshop.service.Impl;

import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.Cart;
import com.myproject.simpleonlineshop.repository.CartRepository;
import com.myproject.simpleonlineshop.service.CartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart getCartById(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such Cart found"));
        // if the cart has multiple items we are going to calculate the totalCartPrice and then save it with new totalPrice
        BigDecimal totalCartPrice = cart.getTotalCartPrice();
        cart.setTotalCartPrice(totalCartPrice);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCartById(Long id) {

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        return null;
    }

    @Override
    public Long initilizeCart() {
        return 0L;
    }
}
