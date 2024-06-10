package com.example.demolibrarymanagement.service.impl;

import com.example.demolibrarymanagement.DTO.request.FilterRequest;
import com.example.demolibrarymanagement.DTO.request.UpsertBook;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.repository.AuthorRepository;
import com.example.demolibrarymanagement.repository.BookRepository;
import com.example.demolibrarymanagement.repository.CategoryRepository;
import com.example.demolibrarymanagement.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {
    public final BookRepository bookRepository;
    public final AuthorRepository authorRepository;
    public final CategoryRepository categoryRepository;
    private  final ModelMapper modelMapper;

    @Override
    @Transactional
    public Page<Book> getAllBook(FilterRequest filterRequest, Pageable pageable){

        return bookRepository.findByFilter(filterRequest,pageable);
    }

    @Override
    @Transactional
    public Book createBook(UpsertBook upsertBook) {
        Book book = modelMapper.map(upsertBook, Book.class);
        book.setAuthor(authorRepository.findAuthorById(upsertBook.getAuthor()));
        book.setCategory(categoryRepository.findCategoryById(upsertBook.getCategory()));
        book.setCreatedAt(new Date());
        book.setUpdatedAt(new Date());
        return bookRepository.save(book);
    }

//    @Override
//    @Transactional
//    public Book createBook(UpsertBook upsertBook) {
//        Book book = Book.builder()
//                .title(upsertBook.getTitle())
//                .author(authorRepository.findAuthorById(upsertBook.getAuthorId()))
//                .category(categoryRepository.findCategoryById(upsertBook.getCategoryId()))
//                .quantity(upsertBook.getQuantity())
//                .createdAt(new Date())
//                .updatedAt(new Date())
//                .build();
//        return bookRepository.save(book);
//    }
    @Override
    @Transactional
    public Book updateBook(UpsertBook upsertBook, Integer id) throws DataNotFoundException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find book with id: " + id));
        // Cập nhật thông tin của cuốn sách
        book.setTitle(upsertBook.getTitle());
        book.setAuthor(authorRepository.findAuthorById(upsertBook.getAuthor()));
        book.setCategory(categoryRepository.findCategoryById(upsertBook.getCategory()));
        book.setQuantity(upsertBook.getQuantity());
        // Cập nhật các trường khác nếu cần
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book deleteBook(Integer id) throws DataNotFoundException {
        Book book = bookRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException(String.format("Cannot find product with id: %d", id)));
        bookRepository.delete(book);
        return book;
    }


    @Override
    @Transactional
    public Page<Book> findBookByName(String name, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page , size);
        return bookRepository.findBookByTitleContainingIgnoreCase(name, pageRequest);
    }


}
