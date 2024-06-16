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
public class ExportFilterlRequest {
    String bookName;
    String userName;
    Date bookedDateStart;
    Date bookedDateEnd;
    Date dueDateStart;
    Date dueDateEnd;
}
