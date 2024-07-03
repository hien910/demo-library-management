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

    @Query("SELECT DISTINCT e FROM Email e WHERE DATE(e.sendTime) = CURRENT_DATE AND e.status = false")
    List<Email> findDistinctToEmailsSendTimeToday();

}
