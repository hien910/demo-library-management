package com.example.demolibrarymanagement.service.impl;

import com.example.demolibrarymanagement.DTO.request.BookInfoDTO;
import com.example.demolibrarymanagement.DTO.request.FilterRequest;
import com.example.demolibrarymanagement.DTO.request.GetByAuthorCategoryRequest;
import com.example.demolibrarymanagement.DTO.request.UpsertBook;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Author;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.Category;
import com.example.demolibrarymanagement.model.entity.MostBorrowBook;
import com.example.demolibrarymanagement.repository.AuthorRepository;
import com.example.demolibrarymanagement.repository.BookRepository;
import com.example.demolibrarymanagement.repository.BookedBookRepository;
import com.example.demolibrarymanagement.repository.CategoryRepository;
import com.example.demolibrarymanagement.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {
    public final BookRepository bookRepository;
    public final AuthorRepository authorRepository;
    public final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final BookedBookRepository bookedBookRepository;

    private String filePath = "D:\\sourcecode\\spring_js\\part3\\demo-library-management\\src\\main\\resources\\excel";


    @Override
    public Page<Book> getAllBook(FilterRequest filterRequest, Pageable pageable) {
        return bookRepository.findByFilter(filterRequest, pageable);
    }

    @Override
    public List<MostBorrowBook> getMostBorrowedBooks() {
        return bookRepository.findMostBorrowedBooks();
    }

    @Override
    public List<Book> getBooksRunningOutOfStock(int threshold) {
        return bookRepository.findBooksRunningOutOfStock(threshold);
    }


    @Override
    @Transactional
    public Book createBook(UpsertBook upsertBook) {
        Book book = modelMapper.map(upsertBook, Book.class);
        book.setAuthor(authorRepository.findAuthorById(upsertBook.getAuthor()));
        book.setCategory(categoryRepository.findCategoryById(upsertBook.getCategory()));
        book.setCreatedAt(new Date());
        book.setUpdatedAt(new Date());
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(UpsertBook upsertBook, Integer id) throws DataNotFoundException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find book with id: " + id));
        // Cập nhật thông tin của cuốn sách
        book.setTitle(upsertBook.getTitle());
        book.setAuthor(authorRepository.findAuthorById(upsertBook.getAuthor()));
        book.setCategory(categoryRepository.findCategoryById(upsertBook.getCategory()));
        book.setQuantity(upsertBook.getQuantity());
        // Cập nhật các trường khác nếu cần
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book deleteBook(Integer id) throws DataNotFoundException {
        Book book = bookRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException(String.format("Cannot find product with id: %d", id)));
        bookRepository.delete(book);
        return book;
    }

    @Override
    @Transactional
    public void importExcelFile(MultipartFile file) throws IOException {
        try (Workbook workbookIn = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheetIn = workbookIn.getSheetAt(0);
            int numberOfRows = sheetIn.getPhysicalNumberOfRows();

            List<Book> books = new ArrayList<>();
            String fileFullPath = null;

            // Tạo workbook mới cho file Excel đầu ra
            Workbook workbookOut = new XSSFWorkbook();
            Sheet sheetOut = workbookOut.createSheet("Response");

            List<Author> authorList = authorRepository.findAll();
            List<Author> addNewAuthor = new ArrayList<>();
            List<Category> categoryList = categoryRepository.findAll();
            List<Category> addNewCategory = new ArrayList<>();


            List<String> bookCodeDB = bookRepository.findAllCode();
            Set<String> bookCodelist = new HashSet<>();

            // Đọc dữ liệu từ file Excel và lưu vào danh sách books
            for (int i = 0; i < numberOfRows; i++) {
                Row rowIn = sheetIn.getRow(i);
                if (rowIn == null) continue;
                Row rowOut = sheetOut.createRow(i);
                rowOut.createCell(0).setCellValue((rowIn.getCell(0) != null && rowIn.getCell(0).getCellType() != CellType.BLANK) ? rowIn.getCell(0).toString() : "");
                rowOut.createCell(1).setCellValue((rowIn.getCell(1) != null && rowIn.getCell(1).getCellType() != CellType.BLANK) ? rowIn.getCell(1).toString() : "");
                rowOut.createCell(2).setCellValue((rowIn.getCell(2) != null && rowIn.getCell(2).getCellType() != CellType.BLANK) ? rowIn.getCell(2).toString() : "");
                rowOut.createCell(3).setCellValue((rowIn.getCell(3) != null && rowIn.getCell(3).getCellType() != CellType.BLANK) ? rowIn.getCell(3).toString() : "");
                rowOut.createCell(4).setCellValue((rowIn.getCell(4) != null && rowIn.getCell(4).getCellType() != CellType.BLANK) ? rowIn.getCell(4).toString() : "");
                rowOut.createCell(5).setCellValue((rowIn.getCell(5) != null && rowIn.getCell(5).getCellType() != CellType.BLANK) ? rowIn.getCell(5).toString() : "");

                if (i == 0) {
                    rowOut.createCell(6).setCellValue("Trạng thái");
                    rowOut.createCell(7).setCellValue("Ghi chú");
                    bookCodelist.add("List book code");
                    continue;
                }

                if (bookCodelist.contains(rowIn.getCell(5).getStringCellValue())) {
                    rowOut.createCell(6).setCellValue("Thất bại");
                    rowOut.createCell(7).setCellValue("Book code đã có trước đó");
                    continue;
                }
                bookCodelist.add(rowIn.getCell(5).getStringCellValue());

                boolean status = true;
                for (int j = 0; j <= 5; j++) {
                    if (rowIn.getCell(j) == null || rowIn.getCell(j).getCellType() == CellType.BLANK) {
                        status = false;
                        rowOut.createCell(6).setCellValue("Thất bại");
                        rowOut.createCell(7).setCellValue("Dữ liệu để trống!");
                        break;
                    }
                }
                if (!status) {
                    continue;
                }

                // check dư liệu số lượng
                Cell cell = rowIn.getCell(2); // Lấy ô cột thứ 3 (0-based index)
                int quantity;
                if (cell.getCellType() == CellType.NUMERIC) {
                    double numericValue = cell.getNumericCellValue();
                    if (!(numericValue == Math.floor(numericValue))) {
                        // Giá trị không phải là số nguyên
                        rowOut.createCell(6).setCellValue("Thất bại");
                        rowOut.createCell(7).setCellValue("Số lượng không phải là số nguyên!");
                        continue;
                    } else {
                        quantity = (int) numericValue;
                    }
                } else if (cell.getCellType() == CellType.STRING) {
                    // Xử lý cho kiểu dữ liệu STRING
                    String stringValue = cell.getStringCellValue();
                    try {
                        quantity = Integer.parseInt(stringValue);
                    } catch (NumberFormatException e) {
                        // Xử lý khi chuỗi không thể chuyển đổi thành số nguyên
                        rowOut.createCell(6).setCellValue("Thất bại");
                        rowOut.createCell(7).setCellValue("Dữ liệu số lượng không đúng định dạng!");
                        continue;
                    }
                } else {
                    // Kiểu dữ liệu của ô không phải là numeric
                    rowOut.createCell(6).setCellValue("Thất bại");
                    rowOut.createCell(7).setCellValue("Dữ liệu số lượng không đúng định dạng!");
                    continue;
                }

                if (bookCodeDB.contains(rowIn.getCell(5).getStringCellValue())) {
                    rowOut.createCell(6).setCellValue("Thất bại");
                    rowOut.createCell(7).setCellValue("Sách không hợp lệ");
                    continue;
                } else {
                    String desiredCategoryName = rowIn.getCell(4).getStringCellValue();
                    String desiredAuthorName = rowIn.getCell(3).getStringCellValue();
                    Author foundAuthor = authorList.stream()
                            .filter(a -> a.getName().equals(desiredAuthorName))
                            .findFirst()
                            .orElse(null);;
                    Category foundCategory = categoryList.stream()
                            .filter(category -> category.getName().equals(desiredCategoryName))
                            .findFirst().orElse(null);

                    if (foundAuthor==null) foundAuthor = Author.builder().name(desiredAuthorName).build();
                    if (foundCategory==null) foundCategory = Category.builder().name(desiredCategoryName).build();
                    addNewAuthor.add(foundAuthor);
                    addNewCategory.add(foundCategory);
                    Book newBook = Book.builder()
                            .author(foundAuthor)
                            .category(foundCategory)
                            .title(rowIn.getCell(1).getStringCellValue())
                            .code(rowIn.getCell(5).getStringCellValue())
                            .quantity(quantity)
                            .createdAt(new Date())
                            .updatedAt(new Date())
                            .build();
                    books.add(newBook);
                    rowOut.createCell(6).setCellValue("Thành công");
                    rowOut.createCell(7).setCellValue("Đã thêm sách mới!");
                }
            }
            bookRepository.saveAll(books);
            authorRepository.saveAll(addNewAuthor);
            categoryRepository.saveAll(addNewCategory);
            String fileName = "ExcelResponse.xlsx";
            fileFullPath = filePath + File.separator + fileName;

            // Kiểm tra và đổi tên file nếu đã tồn tại
            File fileReponse = new File(fileFullPath);
            int fileCount = 1;
            while (fileReponse.exists()) {
                fileName = "ExcelResponse" + fileCount + ".xlsx";
                fileFullPath = filePath + File.separator + fileName;
                fileReponse = new File(fileFullPath);
                fileCount++;
            }
            // Ghi workbook ra file hệ thống
            try (FileOutputStream fileOut = new FileOutputStream(fileFullPath)) {
                workbookOut.write(fileOut);
                workbookOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public List<BookInfoDTO> getBookInfo(GetByAuthorCategoryRequest request) {
        return bookRepository.getBookInfo(request.getCategory(), request.getAuthor());
    }
}
