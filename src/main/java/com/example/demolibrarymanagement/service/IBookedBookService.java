package com.example.demolibrarymanagement.service;


import com.example.demolibrarymanagement.DTO.request.UpsertBorrowRequest;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.BookedBook;

import java.util.Date;

public interface IBookedBookService  {
    BookedBook createBooked(UpsertBorrowRequest request) throws DataNotFoundException;

    BookedBook updateBooked(Integer id) throws DataNotFoundException;

}
