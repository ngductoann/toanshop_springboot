package com.toan.toanshop.service.Impl;

import com.toan.toanshop.Exception.AlreadyExistsException;
import com.toan.toanshop.Exception.ResourceNotFoundException;
import com.toan.toanshop.model.Category;
import com.toan.toanshop.repository.CategoryRepository;
import com.toan.toanshop.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired public final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
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
        return Optional.of(category)
                .filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(
                        () -> new AlreadyExistsException(category.getName() + " already exists"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(
                        oldCategory -> {
                            oldCategory.setName(category.getName());
                            return categoryRepository.save(oldCategory);
                        })
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository
                .findById(id)
                .ifPresentOrElse(
                        categoryRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException("Category not found!");
                        });
    }
}
