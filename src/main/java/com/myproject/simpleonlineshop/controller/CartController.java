package com.myproject.simpleonlineshop.controller;


import com.myproject.simpleonlineshop.dto.ApiResponse;
import com.myproject.simpleonlineshop.dto.CartDto;
import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.mapper.MyModelMapper;
import com.myproject.simpleonlineshop.model.Cart;
import com.myproject.simpleonlineshop.service.CartService;
import org.hibernate.annotations.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final CartService cartService;
    private final MyModelMapper modelMapper;

    public CartController(CartService cartService, MyModelMapper modelMapper) {
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable("cartId") Long cartId){
        try {
            Cart cart = cartService.getCartById(cartId);
            CartDto cartDto = modelMapper.toCartDto(cart);
            return ResponseEntity.ok(new ApiResponse("Cart Found", cartDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            new ApiResponse( e.getMessage(), null)
                    );
        }
    }
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable("cartId") Long id){
        try {
            cartService.clearCartById(id);
            return ResponseEntity.ok(new ApiResponse("cart id cleared", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
            //getCartById() is throwing some message
        }
    }

    @GetMapping("/{cartId}/getTotalAmount")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable("cartId") Long id){
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(id);
            return ResponseEntity.ok(new ApiResponse("here is the Total Price", totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
            //getCartById() is throwing some message
        }
    }
}
