package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findAuthorById(Integer id);
    Author findAuthorByName(String name);
    @Query("SELECT lower(a.name) FROM Author a")
    List<String> findAllAuthorName();
}
