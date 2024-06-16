package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findCategoryById(Integer id);

    Category findCategoryByName(String name);
}
