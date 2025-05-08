package com.myproject.simpleonlineshop.controller;


import com.myproject.simpleonlineshop.dto.AddProductRequestDto;
import com.myproject.simpleonlineshop.dto.ApiResponse;
import com.myproject.simpleonlineshop.dto.UpdateProductRequestDto;
import com.myproject.simpleonlineshop.exception.AlreadyExistsException;
import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.Product;
import com.myproject.simpleonlineshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${api.prefix}/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("Success", allProducts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable("id") Long id){
        try {
            Product productById = productService.getProductById(id);

            return ResponseEntity.ok(new ApiResponse("Found", productById));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequestDto product){
        try {
            Product theProduct = productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Product Created Successfully", theProduct));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", e.getMessage()));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody UpdateProductRequestDto product){
        try {
            Product theProduct = productService.updateProduct(product,id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse("Created", theProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", e.getMessage()));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Long id){
        try {
            productService.deleteProductById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", e.getMessage()));
        }
    }
    @GetMapping("/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(
            @RequestParam String brand,
            @RequestParam String name
    ){
        try {
            List<Product> productsByBrandAndName = productService.getProductsByBrandAndName(brand, name);
            if(productsByBrandAndName.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Not Found", null));
            }

            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("Found",productsByBrandAndName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", e.getMessage()));
        }

    }
    @GetMapping("/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(
            @RequestParam String category,
            @RequestParam String brand
    ){
        try {
            List<Product> productsByCategoryAndBrand = productService.getProductsByCategoryAndBrand(category, brand);
            // method implemented in service doesn't provide any exception so
            if(productsByCategoryAndBrand.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Not Found", null));
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("Found",productsByCategoryAndBrand));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", e.getMessage()));
        }

    }
    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse> getProductByName(
            @PathVariable("name") String name
    ){
        try {
            List<Product> productsByName = productService.getProductsByName(name);
            // method implemented in service doesn't provide any exception so
            if(productsByName.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Not Found", null));
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("Found",productsByName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", e.getMessage()));
        }

    }
    @GetMapping("/brand")
    public ResponseEntity<ApiResponse> getProductByBrand(
            @RequestParam String brand
    ){
        try {
            List<Product> productsByBrand = productService.getProductsByBrand(brand);
            // method implemented in service doesn't provide any exception so
            if(productsByBrand.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse( "product with brandName: " +brand + " NOT Found", null));
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("product with brandName: " +brand + " Found",productsByBrand));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", e.getMessage()));
        }

    }
    @GetMapping("/category")
    public ResponseEntity<ApiResponse> getProductByCategory(
            @RequestParam String category
    ){
        try {
            List<Product> productsByCategory = productService.getProductsByCategory(category);
            // method implemented in service doesn't provide any exception so
            if(productsByCategory.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Not Found", null));
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("Found",productsByCategory));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", e.getMessage()));
        }

    }
    @GetMapping("/countBy/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsAmountByBrandAndName(
            @RequestParam String brand,
            @RequestParam String name
    ){
        try {
            var products = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("Product Count",products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", e.getMessage()));
        }

    }






}
