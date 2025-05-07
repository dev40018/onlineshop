package com.myproject.simpleonlineshop.controller;


import com.myproject.simpleonlineshop.dto.ApiResponse;
import com.myproject.simpleonlineshop.exception.AlreadyExistsException;
import com.myproject.simpleonlineshop.model.Category;
import com.myproject.simpleonlineshop.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found", categories));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        try {
            Category theCategory = categoryService.addCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Category Added", theCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
            /*
            Concurrent Updates: This is a classic example. If two users attempt to edit the same resource simultaneously, the second user attempting to save their changes might receive a 409 Conflict if the first user's changes have already altered the resource's state.  
                Versioning Conflicts: When systems use version control for resources, a 409 Conflict can occur if a client tries to PUT (update or replace) an older version of a resource, while a newer version already exists on the server. This prevents accidental overwrites of more recent changes.
                Creating an Existing Resource: Attempting to create a resource (e.g., via a POST or PUT request) that already exists and is not allowed to be duplicated can trigger a 409 Conflict. For instance, trying to create a user account with a username that is already taken.
                State Mismatches: If a client requests an operation that is not permissible given the resource's current state (e.g., trying to delete a resource that is already marked as deleted or is locked for modifications).
                Data Integrity Violations: The request might violate predefined rules or constraints for the data, such as attempting to establish a relationship that would create an invalid state in a database.
             */
        }
    }

}
