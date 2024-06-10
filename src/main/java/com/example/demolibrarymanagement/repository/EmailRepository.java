package com.example.demolibrarymanagement.repository;

import
        com.example.demolibrarymanagement.model.entity.Email;
import com.example.demolibrarymanagement.model.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EmailRepository extends JpaRepository<Email, Integer> {

    @Query("SELECT e FROM Email e WHERE FUNCTION('DATE', e.sendTime) = FUNCTION('DATE', :currentDate) AND e.status = true")
    List<Email> findEmailsSentToday(@Param("currentDate") Date currentDate);

    List<Email> findByStatus(Boolean status);

//    @Query("SELECT e FROM Email e WHERE e.typeMail = :typeMail " +
//            "AND e.toEmail = :toEmail " +
//            "AND e.status = false " +
//            "AND DATE(e.createdAt) = CURRENT_DATE")
//    List<Email> findByTypeMailAndToEmailAndStatusAndCreatedAtToday(@Param("typeMail") TypeMail typeMail, @Param("toEmail") String toEmail);
//    @Query("SELECT e FROM Email e WHERE  DATE(e.createdAt) = (CURRENT_DATE) AND e.status = false")
//    List<Email> findByCreatedAtIsToday();

    @Query("SELECT DISTINCT e FROM Email e WHERE DATE(e.sendTime) = CURRENT_DATE AND e.status = false")
    List<Email> findDistinctToEmailsSendTimeToday();

//    @Query("SELECT bb.id FROM Email e JOIN e.bookedBook bb WHERE DATE(e.createdAt) = CURRENT_DATE")
//    List<Integer> findBookedBookIdsCreatedByToday();

    @Query(value = "SELECT u.id FROM users u " +
            "WHERE u.email IN (SELECT DISTINCT e.to_email FROM emails e " +
            "WHERE DATE(e.created_at) = CURRENT_DATE)"
            , nativeQuery = true)
    List<Integer> findUserIdsWithEmailsCreatedToday();
}
