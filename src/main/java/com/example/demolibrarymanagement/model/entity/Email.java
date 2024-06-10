package com.example.demolibrarymanagement.model.entity;

//import com.example.demolibrarymanagement.model.enums.TypeMail;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String subject;

    @Column(columnDefinition = "TEXT")
    String content;

    String toUser;
    String toEmail;

    @Column(name = "send_time")
    Date sendTime;


    Boolean status;
    Date createdAt;
    Date updatedAt;

}