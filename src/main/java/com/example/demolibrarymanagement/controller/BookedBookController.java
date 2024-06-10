package com.example.demolibrarymanagement.controller;

import com.example.demolibrarymanagement.DTO.request.UpsertBorrowRequest;
import com.example.demolibrarymanagement.DTO.response.Response;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.BookedBook;
import com.example.demolibrarymanagement.repository.BookRepository;
import com.example.demolibrarymanagement.repository.BookedBookRepository;
import com.example.demolibrarymanagement.service.IBookedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booked")
public class BookedBookController {

    private final IBookedBookService bookedBookService;
    private final BookRepository bookRepository;
    private final BookedBookRepository bookedBookRepository;

    @PostMapping("/create")
    public ResponseEntity<Response<BookedBook>> createBooked(@RequestBody UpsertBorrowRequest request) throws DataNotFoundException {
//        // check quá số lượng sách tối đa cho mượn
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User loginUSer = (User) authentication.getPrincipal();
//        List<BookedBook> bookedBookList = bookedBookRepository.findBookedBookByUser_IdAndStatus(loginUSer.getId(), false);
//        Integer numBorrowBook = 0;
//        for (BookedBook b: bookedBookList) {
//            numBorrowBook += b.getQuantity();
//        }
//        int checkMaxBooked = numBorrowBook + request.getQuantity();
//        if(checkMaxBooked > 5){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new Response<>("400", "Request to borrow books in excess of the allowed number.", null));
//        }

        try {
            BookedBook bookedBook = bookedBookService.createBooked(request);
            Response<BookedBook> response = new Response<>("201", "Tạo đặt sách thành công", bookedBook);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>("500", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("500", "An unexpected error occurred", null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response<BookedBook>> updateBooked(@RequestParam Integer id) throws DataNotFoundException {
        BookedBook bookedBook = bookedBookService.updateBooked( id);

        Response<BookedBook> response = new Response<>("200", "Booked book updated successfully", bookedBook);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
