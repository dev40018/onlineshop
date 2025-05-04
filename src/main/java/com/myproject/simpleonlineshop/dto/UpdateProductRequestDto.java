package com.myproject.simpleonlineshop.dto;

import com.myproject.simpleonlineshop.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequestDto {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int quantityInInventory;

    private Category category;
}
