package com.myproject.simpleonlineshop.service.Impl;

import com.myproject.simpleonlineshop.dto.AddProductRequestDto;
import com.myproject.simpleonlineshop.dto.UpdateProductRequestDto;
import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.Category;
import com.myproject.simpleonlineshop.model.Product;
import com.myproject.simpleonlineshop.repository.CategoryRepository;
import com.myproject.simpleonlineshop.repository.ProductRepository;
import com.myproject.simpleonlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
// @RequiredArgsConstructor you can use this to automate constructor-based injection
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Product addProduct(AddProductRequestDto addProductRequestDto) {


        //check if the category is found in database, if yes, set it as a new product category
        Category category = Optional.ofNullable(categoryRepository.findByName(addProductRequestDto.getCategory().getName()))
                // if no, save it as a new category
                .orElseGet(() -> {
                    Category newCategory = new Category(addProductRequestDto.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        //then set it as a new product category
        addProductRequestDto.setCategory(category);
        return productRepository.save(toProduct(addProductRequestDto, category));
    }

    private Product toProduct(AddProductRequestDto productRequestDto, Category category){
        return new Product(
                productRequestDto.getName(),
                productRequestDto.getBrand(),
                productRequestDto.getDescription(),
                productRequestDto.getPrice(),
                productRequestDto.getQuantityInInventory(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + "doesn't exists"));
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(
                        productRepository::delete,
                        () -> {throw new ResourceNotFoundException("no such product");}
                );
    }

    @Override
    @Transactional
    public Product updateProduct(UpdateProductRequestDto updateProductRequest, Long id) {
        return productRepository.findById(id).map(existingProduct -> updateExistingProduct(existingProduct, updateProductRequest))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));
    }
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequestDto requestDto){
        existingProduct.setName(requestDto.getName());
        existingProduct.setBrand(requestDto.getBrand());
        existingProduct.setDescription(requestDto.getDescription());
        existingProduct.setPrice(requestDto.getPrice());
        existingProduct.setQuantityInInventory(requestDto.getQuantityInInventory());

        Category category = categoryRepository.findByName(requestDto.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }
}
