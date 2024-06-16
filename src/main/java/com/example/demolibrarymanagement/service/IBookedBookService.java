package com.example.demolibrarymanagement.service;


import com.example.demolibrarymanagement.DTO.request.ExportFilterlRequest;
import com.example.demolibrarymanagement.DTO.request.UpsertBorrowRequest;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.BookedBook;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IBookedBookService  {
    List<BookedBook> createBooked(List<UpsertBorrowRequest> request) throws DataNotFoundException;

    BookedBook updateBooked(Integer id) throws DataNotFoundException;

    @Transactional
    void exportGetBooked(ExportFilterlRequest request);
}
