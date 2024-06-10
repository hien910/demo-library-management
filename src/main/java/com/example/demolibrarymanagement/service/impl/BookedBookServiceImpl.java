package com.example.demolibrarymanagement.service.impl;

import com.example.demolibrarymanagement.DTO.request.UpsertBorrowRequest;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.BookedBook;
import com.example.demolibrarymanagement.repository.BookRepository;
import com.example.demolibrarymanagement.repository.BookedBookRepository;
import com.example.demolibrarymanagement.service.IBookedBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

//         Kiểm tra số lượng sách còn đủ không
        if (book.getQuantity() < request.getQuantity()) {
            throw new DataNotFoundException("Not enough quantity available");
        }
        BookedBook bookedBook = modelMapper.map(request, BookedBook.class);
        bookedBook.setBook(book);
        bookedBook.setIsReturn(false);

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
