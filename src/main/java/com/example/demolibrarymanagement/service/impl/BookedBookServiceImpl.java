package com.example.demolibrarymanagement.service.impl;

import com.example.demolibrarymanagement.DTO.request.UpsertBorrowRequest;
import com.example.demolibrarymanagement.DTO.response.Response;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.BookedBook;
import com.example.demolibrarymanagement.model.entity.User;
import com.example.demolibrarymanagement.repository.BookRepository;
import com.example.demolibrarymanagement.repository.BookedBookRepository;
import com.example.demolibrarymanagement.service.IBookedBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookedBookServiceImpl implements IBookedBookService {
    private final BookedBookRepository bookedBookRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookedBook createBooked(UpsertBorrowRequest request) throws DataNotFoundException {
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(()
                -> new DataNotFoundException("Cannot find book with id: " + request.getBookId()));
        // check quá số lượng sách tối đa cho mượn
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUSer = (User) authentication.getPrincipal();
        List<BookedBook> bookedBookList = bookedBookRepository.findBookedBookByUser_IdAndIsReturn(loginUSer.getId(), false);
        System.out.println(bookedBookList.size());
        Integer numBorrowBook = 0;
        for (BookedBook b: bookedBookList) {
            numBorrowBook += b.getQuantity();
        }
        int checkMaxBooked = numBorrowBook + request.getQuantity();
        if(checkMaxBooked > 5){
            throw new DataNotFoundException("Request to borrow books in excess of the allowed number.");
        }

        //   Kiểm tra số lượng sách còn đủ không
        if (book.getQuantity() < request.getQuantity()) {
            throw new DataNotFoundException("Not enough quantity available");
        }
        BookedBook bookedBook = modelMapper.map(request, BookedBook.class);
        bookedBook.setBook(book);
        bookedBook.setIsReturn(false);
        bookedBook.setUser(loginUSer);
        book.setQuantity(book.getQuantity() - request.getQuantity());
        bookRepository.save(book);
        return bookedBookRepository.save(bookedBook);
    }

//    @Override
//    @Transactional
//    public BookedBook createBooked(UpsertBorrowRequest request) throws DataNotFoundException {
//        Book book = bookRepository.findById(request.getBookId()).orElseThrow(()
//                -> new DataNotFoundException("Cannot find book with id: " + request.getBookId()));
//
//        // Kiểm tra số lượng sách còn đủ không
//        if (book.getQuantity() < request.getQuantity()) {
//            throw new DataNotFoundException("Not enough quantity available");
//        }
//
//        BookedBook bookedBook = BookedBook.builder()
//                .book(book)
//                .bookedDate(request.getBookedDate())
//                .dueDate(request.getDueDate())
//                .quantity(request.getQuantity())
//                .status(false)
//                .build();
//        book.setQuantity(book.getQuantity() - request.getQuantity());
//        bookRepository.save(book);
//        return bookedBookRepository.save(bookedBook);
//    }

    @Override
    @Transactional
    public BookedBook updateBooked(Integer id) throws DataNotFoundException {
        BookedBook bookedBook = bookedBookRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Cannot find book with id: " + id));
        bookedBook.setIsReturn(true);
        bookedBook.setReturnDate(new Date());

        Book book = bookRepository.findBookById(bookedBook.getBook().getId());
        book.setQuantity(bookedBook.getQuantity() + book.getQuantity());
        bookRepository.save(book);
        return bookedBookRepository.save(bookedBook);
    }

}
