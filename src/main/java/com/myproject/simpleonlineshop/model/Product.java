package com.myproject.simpleonlineshop.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "t_product")
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int quantityInInventory;

    //relationships
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    /*
    mappedBy="name of attribute inside image entity"
    CascadeType.ALL -> every op on product entity also getting applied to related image entity
    orphanRemoval = true -> if there is image without referencing its parent it database it will be removed
     */
    private List<Image> images;

    public Product(String name,
                   String brand,
                   String description,
                   BigDecimal price,
                   int quantityInInventory,
                   Category category) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.quantityInInventory = quantityInInventory;
        this.category = category;
    }

}