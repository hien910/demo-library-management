package com.example.demolibrarymanagement.DTO.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionStatusDTO implements Serializable {
    String status;
    String message;
    String data;
}
