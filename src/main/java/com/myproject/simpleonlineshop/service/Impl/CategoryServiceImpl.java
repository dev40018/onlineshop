package com.myproject.simpleonlineshop.service.Impl;

import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.Category;
import com.myproject.simpleonlineshop.repository.CategoryRepository;
import com.myproject.simpleonlineshop.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found "));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return   return Optional.of(category)
                .filter(checkingCategory -> !categoryRepository.existsByName(checkingCategory.getName()) )
                .map(c -> categoryRepository.save(c))
                .orElseThrow(() -> new AlreadyExistsException(category.getName() + "category Already Exists"));;
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(() -> new ResourceNotFoundException("No such Category Exists"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(
                categoryRepository::delete,
                () -> {throw new ResourceNotFoundException("No such Category exists");}
        );
    }
}
