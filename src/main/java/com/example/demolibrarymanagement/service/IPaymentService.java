package com.example.demolibrarymanagement.service;

import com.example.demolibrarymanagement.DTO.request.PaymentDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface IPaymentService {
    PaymentDTO createPayment(HttpServletRequest req, Integer bookedBookId) throws UnsupportedEncodingException;
}
