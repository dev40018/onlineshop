package com.myproject.simpleonlineshop.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "t_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    //mappedBy="name of attribute inside product entity"
    private List<Product> products;

    public Category(String name){
        this.name = name;
    }
}
