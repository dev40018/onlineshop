package com.myproject.simpleonlineshop.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class OrderItemsDto {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}