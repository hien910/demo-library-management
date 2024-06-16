package com.example.demolibrarymanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "booked_books")
public class BookedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name="book_id")
    Book book;

    @Column(name = "booked_date")
    Date bookedDate;

    @Column(name = "due_date")
    Date dueDate;

    @Column(name = "return_date")
    Date returnDate;

    Integer quantity;

    @Column(name = "is_return")
    Boolean isReturn;
}
