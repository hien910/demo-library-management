package com.example.demolibrarymanagement.service.impl;
import com.example.demolibrarymanagement.DTO.request.UpsertBook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Service
public class ExcelService {

//    public List<UpsertBook> parseExcelFile(MultipartFile file) throws IOException {
//        List<UpsertBook> upsertBooks = new ArrayList<>();
//
//        try (InputStream is = file.getInputStream();
//             Workbook workbook = new XSSFWorkbook(is)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                // Skip header row
//                if (row.getRowNum() == 0) {
//                    continue;
//                }
//                UpsertBook upsertBook = new UpsertBook();
//                upsertBook.setTitle(row.getCell(0).getStringCellValue());
//                upsertBook.setQuantityAdd((int) row.getCell(1).getNumericCellValue());
//                upsertBook.setAuthor(row.getCell(2).getStringCellValue());
//                upsertBook.setCategory(row.getCell(3).getStringCellValue());
//                // Add other fields as necessary
//
//                upsertBooks.add(upsertBook);
//            }
//        }
//
//        return upsertBooks;
//    }
}
