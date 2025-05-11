package com.myproject.simpleonlineshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // we can order more than one product in a cartItem like 2 Sony Phones
    private int quantity;
    // each product price
    private BigDecimal unitPrice;
    private BigDecimal totalCartItemPrice;

    //each cartItem represents a product, MANY Items belongs to ONE product, One product belongs to many CartItems
    // it means you can have 3 Pixel 9 in your items, each has a unitPrice, then 3 of them has totalPrice and these 3 belong to 1 Cart
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public  void  setTotalCartItemPrice(){
        // totalCartItemPrice = unitPrice * quantity
        this.totalCartItemPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }
}
