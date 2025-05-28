package com.myproject.simpleonlineshop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_cart")
public class Cart {

    /*
since you are using a cartItemId generator in cart initializeCart() in CartServiceImpl
NOTE: this cause concurrency problems because both ops trying to change same field
 */
    @Id
    private Long id;

    // we give it default value of zero
    private BigDecimal totalCartPrice = BigDecimal.ZERO;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();


    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    // we are defining these methods as a specific behaviour of this class
    public void addItem(CartItem item) {
        this.cartItems.add(item);
        item.setCart(this);
        updateTotalCartPrice();
    }

    /*
     *  `this.items.add(item)` adds the new `CartItem` to the cart's collection of items
     *  `this` refers to the current Cart instance
     *  The item is added to the `items` Set

     *  **Establishing Bidirectional Relationship**
     *  `item.setCart(this)` sets up a two-way link between the cart and the item
     *  This ensures that each CartItem knows which Cart it belongs to
     */

    public void removeItem(CartItem item) {
        this.cartItems.remove(item);
        item.setCart(null);
        updateTotalCartPrice();
    }

    private void updateTotalCartPrice() {
        // take each item
        this.totalCartPrice = this.cartItems.stream().map(item -> {
            //get each item's unitPrice
            BigDecimal unitPrice = item.getUnitPrice();
            if (unitPrice == null) {
                return  BigDecimal.ZERO;
            }
            // valueOf() returns a BigDecimal Object which is required to multiplying 2 operands
            return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            // BigDecimal.ZERO The identity value is both the initial value and the default result for empty streams
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
/*

 **Updating Total Amount**
 - `updateTotalAmount()` recalculates the entire cart's total price

 The `this` Keyword Explained:
 - `this` is a reference to the current instance of the class
 - It helps distinguish between instance variables and method parameters
 - In this context, `this.items` explicitly refers to the cart's `items` collection
 - When you pass `this` to `setCart()`, you're saying "set the cart of this item to the current cart instance"

 Analogy:
 Think of this like adding an item to a shopping basket:
 - You put the item in the basket (`add()`)
 - You attach a tag to the item showing which basket it belongs to (`setCart()`)

 */

