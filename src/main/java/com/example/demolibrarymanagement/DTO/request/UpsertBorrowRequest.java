package com.example.demolibrarymanagement.DTO.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertBorrowRequest {
    Date bookedDate;
    Date dueDate;
    Integer quantity;
    Integer bookId;
}
