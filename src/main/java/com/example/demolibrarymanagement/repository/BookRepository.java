package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.DTO.request.FilterRequest;
import com.example.demolibrarymanagement.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Page<Book> findAll(Pageable pageable);
    Book findBookByTitle (String title);
    Book findBookById(Integer id);

    Page<Book> findBookByTitleContainingIgnoreCase(String title, Pageable pageable);



    @Query(value =
            "SELECT b FROM Book b " +
                    "LEFT JOIN b.author a " +
                    "LEFT JOIN b.category c " +
                    "WHERE " +
                    "(:#{#request.title} IS NULL OR :#{#request.title} = '' OR lower(b.title) LIKE concat('%', lower(:#{#request.title}), '%')) AND " +
                    "((:#{#request.minQuantity} IS NOT NULL AND :#{#request.maxQuantity} IS NOT NULL AND b.quantity BETWEEN :#{#request.minQuantity} AND :#{#request.maxQuantity}) OR " +
                    "(:#{#request.minQuantity} IS NOT NULL AND :#{#request.maxQuantity} IS NULL AND b.quantity >= :#{#request.minQuantity}) OR " +
                    "(:#{#request.minQuantity} IS NULL AND :#{#request.maxQuantity} IS NOT NULL AND b.quantity <= :#{#request.maxQuantity})) AND " +
                    "(:#{#request.author} IS NULL OR lower(a.name) LIKE concat('%', lower(:#{#request.author}), '%')) AND " +
                    "(:#{#request.category} IS NULL OR lower(c.name) LIKE concat('%', lower(:#{#request.category}), '%')) "
    )
    Page<Book> findByFilter(FilterRequest request, Pageable pageable);

    @Query(value =
            "SELECT b.id, COUNT(bb.quantity) AS borrowCount " +
                    "FROM BookedBook bb " +
                    "JOIN bb.book b " +
                    "GROUP BY b.id " +
                    "ORDER BY borrowCount DESC"
    )
    List<Book> findMostBorrowedBooks(Pageable pageable);

}
