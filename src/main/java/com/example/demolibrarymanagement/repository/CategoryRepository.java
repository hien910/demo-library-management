package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findCategoryById(Integer id);

    Category findCategoryByName(String name);
    @Query("SELECT LOWER( c.name) FROM Category c")
    List<String> findAllCategoryName();
}
