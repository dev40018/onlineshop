package com.myproject.simpleonlineshop.dto;

import com.myproject.simpleonlineshop.model.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int quantityInInventory;

    private Category category;
    private List<ImageDto> images;
}
