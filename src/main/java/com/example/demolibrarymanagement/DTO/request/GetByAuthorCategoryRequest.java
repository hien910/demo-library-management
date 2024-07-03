package com.example.demolibrarymanagement.DTO.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetByAuthorCategoryRequest {
    String author;
    String category;
}
