package com.example.demolibrarymanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "token_confirm")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String token;
    LocalDateTime createdAt;
    LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user; // token này của user nào
}
