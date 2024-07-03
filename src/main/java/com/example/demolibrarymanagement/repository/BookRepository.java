package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.DTO.request.BookInfoDTO;
import com.example.demolibrarymanagement.DTO.request.FilterRequest;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Author;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.Category;
import com.example.demolibrarymanagement.model.entity.MostBorrowBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.author.name = :author AND b.category.name = :category AND b.code = :code")
    Book findByTitleAuthorCategoryAndCode(String title, String author, String category, String code);

    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.author.name = :author AND b.category.name = :category")
    Book findByTitleAuthorCategory(String title, String author, String category);

    Book findBookByCode(String code);


//    @Query("SELECT b.book, SUM(b.quantity) as totalQuantity " +
//            "FROM BookedBook b " +
//            "GROUP BY b.book " +
//            "ORDER BY totalQuantity DESC")
//    List<Object[]> findMostBorrowedBooks();

    @Query("SELECT new com.example.demolibrarymanagement.model.entity.MostBorrowBook(b.book, SUM(b.quantity)) " +
            "FROM BookedBook b " +
            "GROUP BY b.book " +
            "HAVING SUM(b.quantity) = (" +
            "    SELECT MAX(maxSum.sumQuantity) FROM (" +
            "        SELECT SUM(b1.quantity) AS sumQuantity " +
            "        FROM BookedBook b1 " +
            "        GROUP BY b1.book" +
            "    ) AS maxSum" +
            ")")
    List<MostBorrowBook> findMostBorrowedBooks();

    @Query("SELECT b.code FROM Book b")
    List<String> findAllCode();
    @Query("SELECT b FROM Book b WHERE b.quantity <= :threshold AND b.quantity > 0 ORDER BY b.quantity ASC")
    List<Book> findBooksRunningOutOfStock(int threshold);
    Book findBookByTitle (String title);
    Book findBookById(Integer id);

    default List<Book> findAllByIdIn(List<Integer> ids) throws DataNotFoundException {
        List<Book> books = new ArrayList<>();
        for (Integer id: ids) {
            Book book  = findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find book with ids: " + id));
            books.add(book);
        }
        return books;
    }

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

    @Procedure(procedureName = "GetBookInfo")
    List<BookInfoDTO> getBookInfo(@Param("p_category") String category,
                                  @Param("p_author") String author);



    @Query(value =
            "SELECT b.id, COUNT(bb.quantity) AS borrowCount " +
                    "FROM BookedBook bb " +
                    "JOIN bb.book b " +
                    "GROUP BY b.id " +
                    "ORDER BY borrowCount DESC"
    )
    List<Book> findMostBorrowedBooks(Pageable pageable);

}
