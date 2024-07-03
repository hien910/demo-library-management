package com.example.demolibrarymanagement.controller;

import com.example.demolibrarymanagement.DTO.request.PaymentDTO;
import com.example.demolibrarymanagement.DTO.request.TransactionStatusDTO;
import com.example.demolibrarymanagement.config.PaymentConfig;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.BookedBook;
import com.example.demolibrarymanagement.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final IPaymentService paymentService;
    @GetMapping("/create")
    public ResponseEntity<?> createPayment(HttpServletRequest req,
                                           @RequestParam Integer bookedBookId) throws UnsupportedEncodingException {
        PaymentDTO paymentDTO = paymentService.createPayment(req,bookedBookId);
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }

    @GetMapping("/payment-infor")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_OrderInfor") String order,
            @RequestParam(value = "vnp_ResponseCode") String response
    ){
        TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
        if (response.equals("00")){
            transactionStatusDTO.setStatus("OK");
            transactionStatusDTO.setMessage("Successfully");
            transactionStatusDTO.setStatus("");
        }else {
            transactionStatusDTO.setStatus("NO");
            transactionStatusDTO.setMessage("False");
            transactionStatusDTO.setStatus("");
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactionStatusDTO);
    }

}
