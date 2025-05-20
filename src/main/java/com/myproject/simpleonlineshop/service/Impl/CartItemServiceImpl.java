package com.myproject.simpleonlineshop.service.Impl;

import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.Cart;
import com.myproject.simpleonlineshop.model.CartItem;
import com.myproject.simpleonlineshop.model.Product;
import com.myproject.simpleonlineshop.repository.CartItemRepository;
import com.myproject.simpleonlineshop.repository.CartRepository;
import com.myproject.simpleonlineshop.service.CartItemService;
import com.myproject.simpleonlineshop.service.CartService;
import com.myproject.simpleonlineshop.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
        //get the cart
        Cart cartById = cartService.getCartById(cartId);

        // get the product
        Product product = productService.getProductById(productId);

        // check if product is already in the cart
        CartItem cartItem = cartById.getCartItems()
                .stream().
                filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());

        //if no initiate a new cartItem
        if(cartItem.getId() == null){
            // in orElse() part, the CartItem is initiated
            cartItem.setProduct(product);
            cartItem.setCart(cartById);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        //if yes increase the quantity
        else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalCartItemPrice();
        cartById.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cartById);

    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cartById = cartService.getCartById(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cartById.removeItem(itemToRemove);
        cartRepository.save(cartById);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cartById = cartService.getCartById(cartId);
        cartById.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(cartItem -> {
                            cartItem.setQuantity(quantity);
                                    cartItem.setUnitPrice(cartItem.getProduct().getPrice());
                                    cartItem.setTotalCartItemPrice();
                        });
        BigDecimal totalCartPrice = cartById.getCartItems().stream().map(CartItem::getTotalCartItemPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        cartById.setTotalCartPrice(totalCartPrice);
        cartRepository.save(cartById);

    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cartById = cartService.getCartById(cartId);
        return cartById.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No Such ProductItem Exists"));
    }
}
