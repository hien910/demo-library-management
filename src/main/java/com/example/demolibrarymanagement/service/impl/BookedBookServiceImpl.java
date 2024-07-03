package com.example.demolibrarymanagement.service.impl;

import com.example.demolibrarymanagement.DTO.request.ExportFilterlRequest;
import com.example.demolibrarymanagement.DTO.request.UpsertBorrowRequest;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.BookedBook;
import com.example.demolibrarymanagement.model.entity.User;
import com.example.demolibrarymanagement.repository.BookRepository;
import com.example.demolibrarymanagement.repository.BookedBookRepository;
import com.example.demolibrarymanagement.service.IBookedBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookedBookServiceImpl implements IBookedBookService {
    private final BookedBookRepository bookedBookRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private String filePath = "D:\\sourcecode\\spring_js\\part3\\demo-library-management\\src\\main\\resources\\excel";

    @Override
    @Transactional
    public List<BookedBook> createBooked(List<UpsertBorrowRequest> request) throws DataNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUSer = (User) authentication.getPrincipal();

        // check quá số lượng sách tối đa cho mượn
        long totalBorrow = bookedBookRepository.findTotalBookedByUserIdAndIsReturn(loginUSer.getId());
        long numBorrowBook = request.stream()
                .mapToLong(UpsertBorrowRequest::getQuantity)
                .sum();
        if((totalBorrow +numBorrowBook) > 5)
            throw new DataNotFoundException("Request to borrow books in excess of the allowed number.");

        //   Kiểm tra số lượng sách còn đủ không
        List<BookedBook> bookedBookList = new ArrayList<>();
        List<Integer> idsBook = request.stream()
                .map(UpsertBorrowRequest::getBookId)
                .toList();
        List<Book> books = bookRepository.findAllByIdIn(idsBook);
        for (int i = 0; i < request.size(); i++) {
            if(books.get(i).getQuantity() < request.get(i).getQuantity())
                throw new DataNotFoundException("Not enough quantity available");

            BookedBook bookedBook = BookedBook.builder()
                    .bookedDate(new Date())
                    .quantity(request.get(i).getQuantity())
                    .book(books.get(i))
                    .bookedDate(new Date())
                    .user(loginUSer)
                    .dueDate(request.get(i).getDueDate())
                    .isReturn(false)
                    .build();
            bookedBookList.add(bookedBook);
            books.get(i).setQuantity(books.get(i).getQuantity() - request.get(i).getQuantity());
            books.get(i).setUpdatedAt(new Date());
        }
        bookRepository.saveAll(books);
        return bookedBookRepository.saveAll(bookedBookList);
    }

    @Override
    @Transactional
    public BookedBook updateBooked(Integer id) throws DataNotFoundException {
        BookedBook bookedBook = bookedBookRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Cannot find booked with id: " + id));
        bookedBook.setIsReturn(true);
        bookedBook.setReturnDate(new Date());

        Book book = bookRepository.findBookById(bookedBook.getBook().getId());
        book.setQuantity(bookedBook.getQuantity() + book.getQuantity());
        bookRepository.save(book);
        return bookedBookRepository.save(bookedBook);
    }
    @Override
    @Transactional
    public void exportGetBooked(ExportFilterlRequest request) {
        List<BookedBook> bookedBookList = bookedBookRepository.findByExportExcelRequest(request);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Booked Books");

        // Tạo hàng tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("STT");
        headerRow.createCell(1).setCellValue("Sách");
        headerRow.createCell(2).setCellValue("Người mượn");
        headerRow.createCell(3).setCellValue("Số lượng mượn");
        headerRow.createCell(4).setCellValue("Ngày mượn");
        headerRow.createCell(5).setCellValue("Ngày trả");
        headerRow.createCell(6).setCellValue("Trạng thái");
        headerRow.createCell(7).setCellValue("Ghi chú");

        // Định dạng ngày với giờ và phút
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle dateTimeCellStyle = workbook.createCellStyle();
        dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm"));


        // Điền dữ liệu vào bảng
        int rowNum = 1;
        int stt = 1;
        String fileFullPath = null;
        for (BookedBook bookedBook : bookedBookList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stt++);
            row.createCell(1).setCellValue(bookedBook.getBook().getTitle());
            row.createCell(2).setCellValue(bookedBook.getUser().getName());
            row.createCell(3).setCellValue(bookedBook.getQuantity());
            Cell bookedDateCell = row.createCell(4);
            bookedDateCell.setCellValue(bookedBook.getBookedDate());
            bookedDateCell.setCellStyle(dateTimeCellStyle);

            Cell dueDateCell = row.createCell(5);
            dueDateCell.setCellValue(bookedBook.getDueDate());
            dueDateCell.setCellStyle(dateTimeCellStyle);

            long overdueDays = (new Date().getTime() - bookedBook.getDueDate().getTime()) / (1000 * 60 * 60 * 24);
            System.out.println(overdueDays);
            if (bookedBook.getIsReturn()) {
                row.createCell(6).setCellValue("Đã trả");
                row.createCell(7).setCellValue("");
            } else if (overdueDays > 0) {
                row.createCell(6).setCellValue("Chưa trả");
                row.createCell(7).setCellValue("Quá hạn");
            } else if (overdueDays > -3 ) {
                row.createCell(6).setCellValue("Chưa trả");
                row.createCell(7).setCellValue("Sắp hết hạn trả");
            } else {
                row.createCell(6).setCellValue("Chưa trả");
                row.createCell(7).setCellValue("");
            }
        }
        String fileName = "GetBookedFilter.xlsx";
        fileFullPath = filePath + File.separator + fileName;

        // Kiểm tra và đổi tên file nếu đã tồn tại
        File file = new File(fileFullPath);
        int fileCount = 1;
        while (file.exists()) {
            fileName = "GetBookedFilter" + fileCount + ".xlsx";
            fileFullPath = filePath + File.separator + fileName;
            file = new File(fileFullPath);
            fileCount++;
        }

        // Ghi workbook ra file hệ thống
        try (FileOutputStream fileOut = new FileOutputStream(fileFullPath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Đóng workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
