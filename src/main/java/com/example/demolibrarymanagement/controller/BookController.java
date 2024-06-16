package com.example.demolibrarymanagement.controller;
import com.example.demolibrarymanagement.DTO.request.FilterRequest;
import com.example.demolibrarymanagement.DTO.request.UpsertBook;
import com.example.demolibrarymanagement.DTO.response.Response;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.service.IBookService;
import com.example.demolibrarymanagement.service.impl.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/book")
public class BookController {
    private final IBookService bookService;
    private final ExcelService excelService;
    @PostMapping()
    public ResponseEntity<Response<Page<Book>>> getAllBook(@RequestBody FilterRequest filterRequest, Pageable pageable){
        Page<Book> books = bookService.getAllBook(filterRequest,pageable);
        return ResponseEntity.ok().body(
                new Response<>("200", "Get all books successfully", books));
    }

    @GetMapping("/most-borrow")
    public ResponseEntity<Response<LinkedHashMap<Book, Integer>>> getMostBorrowedBooks(){
        LinkedHashMap<Book, Integer> mostBorrowedBooks = bookService.getMostBorrowedBooks();
        return ResponseEntity.ok().body(
                new Response<>("200", "Get most borrowed books successfully", mostBorrowedBooks));
    }
    @PostMapping("/running-out")
    public ResponseEntity<Response<List<Book>>> getRunningOut(@RequestParam Integer num){
        return ResponseEntity.ok().body(
                new Response<>("200", "Get most borrowed books successfully"
                        , bookService.getBooksRunningOutOfStock(num)));

    }

    @PostMapping("/create")
    public ResponseEntity<Response<Book>> createBook(@RequestBody UpsertBook upsertBook) throws DataNotFoundException {
        Book book = bookService.createBook(upsertBook);
        Response<Book> response = new Response<>("201", "Book created successfully", book);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Response<Book>> updateBook(@RequestBody UpsertBook upsertBook, @RequestParam Integer id) {
        try {
            Book updatedBook = bookService.updateBook(upsertBook, id);
            Response<Book> response = new Response<>("200", "Book updated successfully", updatedBook);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            Response<Book> response = new Response<>("404", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response<Book>> deleteBook(@RequestParam Integer id) throws DataNotFoundException {
        Book book = bookService.deleteBook(id);
        Response<Book> response = new Response<>("204", "Book deleted successfully", book);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importExcelFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }
        try {
            bookService.importExcelFile(file);
            return new ResponseEntity<>("File imported successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to import file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
