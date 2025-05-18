package com.myproject.simpleonlineshop.controller;




import com.myproject.simpleonlineshop.dto.ApiResponse;
import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.service.CartItemService;
import com.myproject.simpleonlineshop.service.CartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final CartItemService cartItemService;
    private final CartService cartService;

    public CartItemController(CartItemService cartItemService, CartService cartService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }

    @PostMapping("/addItemToCartByCartId")
    public ResponseEntity<ApiResponse> addItemToCart(
            @RequestParam(required = false) Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity){
        try {
            //if there is no cart, create one and then send its id to addItemToCart();
            if(cartId == null){
                cartId = cartService.initilizeCart();
            }
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item Added to Cart", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/cartById/{cartId}/item/{itemId}")
    public ResponseEntity<ApiResponse> removeItemToCart(@PathVariable Long cartId,
                                                        @PathVariable Long itemId
    ){
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Item Removed FROM CartItems List", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/cartById/{cartId}/product/{productId}")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long productId,
                                                          @RequestParam int quantity){
        try {
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item Updated ", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}