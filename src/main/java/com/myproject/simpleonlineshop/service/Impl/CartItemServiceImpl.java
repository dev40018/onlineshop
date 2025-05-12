package com.myproject.simpleonlineshop.service.Impl;

import com.myproject.simpleonlineshop.model.CartItem;
import com.myproject.simpleonlineshop.repository.CartItemRepository;
import com.myproject.simpleonlineshop.repository.CartRepository;
import com.myproject.simpleonlineshop.service.CartItemService;
import com.myproject.simpleonlineshop.service.CartService;
import com.myproject.simpleonlineshop.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CartRepository cartRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, ProductService productService, CartService cartService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {

    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {

    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {

    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        return null;
    }
}
