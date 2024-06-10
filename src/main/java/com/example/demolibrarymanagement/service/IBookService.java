package com.example.demolibrarymanagement.service;

import com.example.demolibrarymanagement.DTO.request.FilterRequest;
import com.example.demolibrarymanagement.DTO.request.UpsertBook;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IBookService {
    @Transactional
    Page<Book> getAllBook(FilterRequest filterRequest, Pageable pageable);

    Book createBook(UpsertBook upsertBook);
    Book deleteBook(Integer id) throws DataNotFoundException;

    Page<Book> findBookByName(String name, Integer page, Integer size);


    Book updateBook(UpsertBook upsertBook, Integer id) throws DataNotFoundException;
}
