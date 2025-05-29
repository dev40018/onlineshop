package com.myproject.simpleonlineshop.service.Impl;

import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.Cart;
import com.myproject.simpleonlineshop.model.User;
import com.myproject.simpleonlineshop.repository.CartItemRepository;
import com.myproject.simpleonlineshop.repository.CartRepository;
import com.myproject.simpleonlineshop.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong generatedCartId = new AtomicLong(0);

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart getCartById(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such Cart found"));
        // if the cart has multiple items we are going to calculate the totalCartPrice and then save it with new totalPrice
        BigDecimal totalCartPrice = cart.getTotalCartPrice();
        cart.setTotalCartPrice(totalCartPrice);
        return cart;
    }

    @Transactional
    @Override
    public void clearCartById(Long id) {
        Cart cart = getCartById(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(cart.getId());
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCartById(id);
        return cart.getTotalCartPrice();

    }

    @Override
    public Cart initilizeCart(User user){
        return Optional.ofNullable(getCartByUserId(user.getId())) // check if there is any cart by that user
                .orElseGet(() -> { // if not, then create a cart and set its user which is passed by method parameter
                        Cart cart = new Cart();
                        cart.setId(generatedCartId.incrementAndGet());
                        cart.setUser(user);
                        return cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
