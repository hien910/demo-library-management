package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findAuthorById(Integer id);
    Author findAuthorByName(String name);
}
