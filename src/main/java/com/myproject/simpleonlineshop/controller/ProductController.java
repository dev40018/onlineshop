package com.myproject.simpleonlineshop.controller;


import com.myproject.simpleonlineshop.dto.AddProductRequestDto;
import com.myproject.simpleonlineshop.dto.ApiResponse;
import com.myproject.simpleonlineshop.dto.ProductDto;
import com.myproject.simpleonlineshop.dto.UpdateProductRequestDto;
import com.myproject.simpleonlineshop.exception.AlreadyExistsException;
import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.mapper.MyModelMapper;
import com.myproject.simpleonlineshop.model.Product;
import com.myproject.simpleonlineshop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "${api.prefix}/products")
public class ProductController {

    private final ProductService productService;
    private final MyModelMapper productMapper;

    public ProductController(ProductService productService, MyModelMapper MyModelMapper) {
        this.productService = productService;
        this.productMapper = MyModelMapper;
    }

    @Transactional
    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> allProducts = productService.getAllProducts();
        List<ProductDto> productDtos = productMapper.getConvertToProductDtos(allProducts);
        return ResponseEntity.ok(new ApiResponse("Success", productDtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable("id") Long id){
        try {
            Product productById = productService.getProductById(id);
            ProductDto productDto = productMapper.toProductDto(productById);
            return ResponseEntity.ok(new ApiResponse("Found", productDto));
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
                    .body(new ApiResponse("Product Updated", theProduct));
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
            List<ProductDto> productDtosByBrandAndName = productsByBrandAndName.stream().map(productMapper::toProductDto).toList();
            if(productDtosByBrandAndName.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Not Found", null));
            }

            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("Found",productDtosByBrandAndName));
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
            List<ProductDto> productDtosByCategoryAndBrand = productsByCategoryAndBrand.stream().map(productMapper::toProductDto).toList();
            // method implemented in service doesn't provide any exception so
            if(productDtosByCategoryAndBrand.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Not Found", null));
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("Found",productDtosByCategoryAndBrand));
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
            List<ProductDto> productDtos = productsByName.stream().map(productMapper::toProductDto).toList();
            // method implemented in service doesn't provide any exception so
            if(productDtos.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Not Found", null));
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("Found",productDtos));
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
            List<ProductDto> productDtos = productsByBrand.stream().map(productMapper::toProductDto).toList();
            // method implemented in service doesn't provide any exception so
            if(productDtos.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse( "product with brandName: " +brand + " NOT Found", null));
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("product with brandName: " +brand + ", Found",productDtos));
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
            List<ProductDto> productDtos = productsByCategory.stream().map(productMapper::toProductDto).toList();
            // method implemented in service doesn't provide any exception so
            if(productDtos.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Not Found", null));
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("Found",productDtos));
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
            Long products = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ApiResponse("Product Count",products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", e.getMessage()));
        }

    }






}
