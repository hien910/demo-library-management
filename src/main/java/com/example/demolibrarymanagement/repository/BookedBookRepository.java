package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.DTO.request.ExportFilterlRequest;
import com.example.demolibrarymanagement.model.entity.BookedBook;
import com.example.demolibrarymanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("SELECT bb.user.id FROM BookedBook bb WHERE bb.id = :bookedBookId")
    Integer findUserIdById(@Param("bookedBookId") Integer bookedBookId);


    @Query("SELECT distinct bb.user FROM BookedBook bb " +
            "WHERE ( DATE(bb.dueDate) < CURRENT_DATE() OR DATEDIFF(bb.dueDate, CURRENT_DATE()) BETWEEN 0 AND 2) " +
            "AND bb.isReturn = false")
    List<User> findDueUser();

    @Query("SELECT SUM(bb.quantity) FROM BookedBook bb " +
            "WHERE bb.isReturn = false AND bb.user.id = :userId")
    Long findTotalBookedByUserIdAndIsReturn(@Param("userId") Integer userId);

    @Query("SELECT bb FROM BookedBook bb " +
            "WHERE (DATE(bb.dueDate) <= CURRENT_DATE() OR DATEDIFF(bb.dueDate, CURRENT_DATE()) BETWEEN 1 AND 2) " +
            "AND bb.isReturn = false " +
            "AND bb.user.id = :userId")
    List<BookedBook> findBookedBooksDueByUserId(@Param("userId") Integer userId);


    @Query(value = "SELECT bb FROM BookedBook bb " +
            "WHERE " +
            "(:#{#request.userName} IS NULL OR :#{#request.userName} = '' OR lower(bb.user.name) LIKE concat('%', lower(:#{#request.userName}), '%')) AND " +
            "(:#{#request.bookName} IS NULL OR :#{#request.bookName} = '' OR lower(bb.user.name) LIKE concat('%', lower(:#{#request.bookName}), '%')) AND " +
            "(" +
            "(:#{#request.bookedDateStart} IS NULL AND :#{#request.bookedDateEnd} IS NOT NULL AND bb.bookedDate <= :#{#request.bookedDateEnd}) OR " +
            "(:#{#request.bookedDateEnd} IS NULL AND :#{#request.bookedDateStart} IS NOT NULL AND bb.bookedDate >= :#{#request.bookedDateStart}) OR " +
            "(:#{#request.bookedDateStart} IS NOT NULL AND :#{#request.bookedDateEnd} IS NOT NULL AND bb.bookedDate BETWEEN :#{#request.bookedDateStart} AND :#{#request.bookedDateEnd}) OR " +
            "(:#{#request.bookedDateStart} IS NULL AND :#{#request.bookedDateEnd} IS NULL)" +
            ") AND " +
            "(" +
            "(:#{#request.dueDateStart} IS NULL AND :#{#request.dueDateEnd} IS NOT NULL AND bb.dueDate <= :#{#request.dueDateEnd}) OR " +
            "(:#{#request.dueDateEnd} IS NULL AND :#{#request.dueDateStart} IS NOT NULL AND bb.dueDate >= :#{#request.dueDateStart}) OR " +
            "(:#{#request.dueDateStart} IS NOT NULL AND :#{#request.dueDateEnd} IS NOT NULL AND bb.dueDate BETWEEN :#{#request.dueDateStart} AND :#{#request.dueDateEnd}) OR " +
            "(:#{#request.dueDateStart} IS NULL AND :#{#request.dueDateEnd} IS NULL)" +
            ")"
    )
    List<BookedBook> findByExportExcelRequest(ExportFilterlRequest request);

    BookedBook findBookedBookById(Integer id);
}

