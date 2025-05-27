package com.myproject.simpleonlineshop.service.Impl;

import com.myproject.simpleonlineshop.dto.OrderDto;
import com.myproject.simpleonlineshop.enums.OrderStatus;
import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.Cart;
import com.myproject.simpleonlineshop.model.Order;
import com.myproject.simpleonlineshop.model.OrderItem;
import com.myproject.simpleonlineshop.model.Product;
import com.myproject.simpleonlineshop.repository.OrderRepository;
import com.myproject.simpleonlineshop.repository.ProductRepository;
import com.myproject.simpleonlineshop.service.CartService;
import com.myproject.simpleonlineshop.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemsList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemsList));
        order.setOrderTotalAmount(calculateTotalAmount(orderItemsList));
        Order savedOrder = orderRepository.save(order);

        // we don't need the cart
        cartService.clearCartById(cart.getId());
        return savedOrder;
    }
    private Order createOrder(Cart cart){
        Order order  = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDateTime(LocalDateTime.now());
        return order;
    }
    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getCartItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setQuantityInInventory(product.getQuantityInInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice(),
                            order,
                            product
                    );
                }).toList();
    }
    private  BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList
                .stream()
                .map(
                        orderItem -> orderItem
                                .getUnitPrice()
                                .multiply(
                                        new BigDecimal(orderItem.getQuantity())
                                )).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public OrderDto getOrder(Long orderId) {
         orderRepository.findById(orderId);
        return null;
    }

    @Override
    public List<Order> getUsersOrder(Long userId){
        return orderRepository.findByUserId(userId);
    }

}
