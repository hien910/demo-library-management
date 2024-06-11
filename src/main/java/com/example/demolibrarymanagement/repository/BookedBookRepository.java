package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.BookedBook;
import com.example.demolibrarymanagement.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookedBookRepository extends JpaRepository<BookedBook, Integer> {

//    @Query("SELECT bb FROM BookedBook bb WHERE bb.isReturn = :status AND bb.dueDate < :currentDate")
//    List<BookedBook> findByStatusAndDueDateBefore(Boolean status, Date currentDate);
//
//    @Query("SELECT bb FROM BookedBook bb WHERE bb.dueDate BETWEEN :date AND :twoDaysLater")
//    List<BookedBook> findByDueDateBetween(Date date, Date twoDaysLater);

//    @Query("SELECT bb.book, SUM(bb.quantity) AS totalBorrowed " +
//            "FROM BookedBook bb " +
//            "GROUP BY bb.book " +
//            "ORDER BY totalBorrowed DESC")
//    List<Book> findMostBorrowedBooks(Pageable pageable);

    BookedBook findBookedBookById(Integer id);
    List<BookedBook> findBookedBookByUserId(Integer userId);
    @Query("SELECT bb.user.id FROM BookedBook bb WHERE bb.id = :bookedBookId")
    Integer findUserIdById(@Param("bookedBookId") Integer bookedBookId);


    //    @Query("SELECT bb.id FROM BookedBook bb WHERE bb.dueDate BETWEEN CURRENT_DATE AND CURRENT_DATE + 2 AND bb.isReturn = false")
//    List<Integer> findBookedBookIdsDueInNextTwoDaysAndNotReturned();
//
//    @Query("SELECT bb.id FROM BookedBook bb WHERE bb.dueDate < CURRENT_DATE AND bb.isReturn = false")
//    List<Integer> findOverdueBookedBookIds();
    @Query(value = "SELECT bb.id FROM BookedBook bb WHERE DATEDIFF(bb.dueDate, CURRENT_DATE()) BETWEEN 1 AND 2 " +
            "AND bb.isReturn = false")
    List<Integer> findBookedBookIdsDueInNextTwoDaysAndNotReturned();

    @Query("SELECT bb.id FROM BookedBook bb WHERE DATE(bb.dueDate) < CURRENT_DATE() AND bb.isReturn = false")
    List<Integer> findOverdueBookedBookIds();

    @Query("SELECT distinct bb.user FROM BookedBook bb " +
            "WHERE ( DATE(bb.dueDate) < CURRENT_DATE() OR DATEDIFF(bb.dueDate, CURRENT_DATE()) BETWEEN 1 AND 2) " +
            "AND bb.isReturn = false" )
    List<User> findDueUser();


    List<BookedBook> findBookedBookByUser_IdAndIsReturn(Integer id, boolean b);
}
