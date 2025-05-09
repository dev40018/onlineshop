package com.myproject.simpleonlineshop.dto;

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
    private CategoryDto category;
    private List<ImageDto> imagesDtos;
}
