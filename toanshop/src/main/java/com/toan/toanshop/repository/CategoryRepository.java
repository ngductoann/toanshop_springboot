package com.toan.toanshop.repository;

import com.toan.toanshop.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Optional<Category> findByName(String name);
    Category findByName(String name);

    Boolean existsByName(String name);
}
