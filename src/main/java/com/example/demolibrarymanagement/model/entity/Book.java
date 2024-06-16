package com.example.demolibrarymanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Objects;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String title;
    Integer quantity;

    String code;

    @ManyToOne
    @JoinColumn(name = "author_id")
    Author author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    Date createdAt;
    Date updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book other)) return false;
        return Objects.equals(this.title, other.title) &&
                Objects.equals(this.author, other.author) &&
                Objects.equals(this.category, other.category) &&
                Objects.equals(this.code, other.code);
    }
}
