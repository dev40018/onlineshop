package com.myproject.simpleonlineshop.dto;

import com.myproject.simpleonlineshop.model.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequestDto {

    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int quantityInInventory;

    private Category category;

}
