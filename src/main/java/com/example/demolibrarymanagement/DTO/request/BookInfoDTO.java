package com.example.demolibrarymanagement.DTO.request;

import lombok.*;

import java.util.Date;

public interface BookInfoDTO {
     String getCategory();
     String getAuthor();
     String getBookName();
     String getUserName();
     Date getBookedDate();
     Date getDueDate();
     Boolean getStatus();
     int getQuantity();
}