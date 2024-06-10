package com.example.demolibrarymanagement.controller;

import com.example.demolibrarymanagement.DTO.response.Response;
import com.example.demolibrarymanagement.model.entity.Author;
import com.example.demolibrarymanagement.model.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/author")
public class AuthorController {

//    @GetMapping()
//    public ResponseEntity<Response<Author>> getAllAuthor(@RequestParam String name){
//
//        return ResponseEntity.ok().body(
//                new Response<>("200", "Get all books successfully", books));
//    }
//    @PostMapping("/create")
//    @PutMapping("/update/{id}")
//    @DeleteMapping("/delete/{id}")
}
