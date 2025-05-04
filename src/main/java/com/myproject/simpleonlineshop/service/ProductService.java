package com.myproject.simpleonlineshop.service;


import com.myproject.simpleonlineshop.dto.AddProductRequestDto;
import com.myproject.simpleonlineshop.dto.UpdateProductRequestDto;
import com.myproject.simpleonlineshop.model.Product;

import java.util.List;

public interface ProductService {


    Product addProduct(AddProductRequestDto addProductRequestDto);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(UpdateProductRequestDto updateProductRequest, Long id);
    Long countProductsByBrandAndName(String brand, String name);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByBrandAndName(String brand, String name);
    List<Product> getProductsByName(String name);
}
