package com.example.demolibrarymanagement.service.impl;

import com.example.demolibrarymanagement.model.entity.BookedBook;
import com.example.demolibrarymanagement.model.entity.Email;
import com.example.demolibrarymanagement.model.entity.User;

import com.example.demolibrarymanagement.repository.BookRepository;
import com.example.demolibrarymanagement.repository.BookedBookRepository;
import com.example.demolibrarymanagement.repository.EmailRepository;
import com.example.demolibrarymanagement.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class MailServiceImpl {
    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final BookedBookRepository bookedBookRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    @Scheduled(cron = "0 7 10 * * ?")
    @Transactional
    public void sendBookedEmail() throws MessagingException, UnsupportedEncodingException {
        List<Email> emails = emailRepository.findDistinctToEmailsSendTimeToday();
        for (Email email: emails) {
            // Gửi email
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("hienjang910@gmail.com", "Library management");
            messageHelper.setTo(email.getToEmail());
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(email.getContent(), true);
            mailSender.send(mimeMessage);
            email.setStatus(true);
            email.setUpdatedAt(new Date());
        }
    }

    @Scheduled(cron = " 0 3 10 * * ?")
    public void createEmailsToSend() {

        List<User> dueUsers = bookedBookRepository.findDueUser();
        List<Email> emailList  = new ArrayList<>();
        for (User user : dueUsers) {
            List<BookedBook> bookedBookList = bookedBookRepository.findBookedBookByUserId(user.getId());
            StringBuilder contentDueSoon = new StringBuilder();
            StringBuilder contentOverDue = new StringBuilder();
            for (BookedBook bookedBook : bookedBookList) {
                long overdueDays = (new Date().getTime() - bookedBook.getDueDate().getTime()) / (1000 * 60 * 60 * 24);
                if (overdueDays < 0) {
                    contentDueSoon.append("<p>Sách mượn sắp hết hạn: ").append(bookedBook.getBook().getTitle())
                            .append(". Số lượng mượn: ").append(bookedBook.getQuantity())
                            .append(". Vui lòng trả trước ngày: ").append(bookedBook.getDueDate()).append(".</p>");
                }
                if (overdueDays > 0) {
                    contentOverDue.append("<p>Sách mượn đã hết hạn: ")
                            .append(bookedBook.getBook().getTitle()).append(". Số lượng mượn: ")
                            .append(bookedBook.getQuantity())
                            .append(", đã quá hạn ").append(overdueDays).append(" ngày!</p>");
                }
            }
            String content = "<p>Xin chào, " + user.getName() + "\n</p>" +
                    "\n<br>" +
                    "<p>" + "Chúng tôi cần thông báo với bạn về trạng thái mượn sách của bạn như sau:" + "\n</p>"
                    + contentDueSoon
                    + contentOverDue +
                    "\n<br>" +
                    "<p>Trân trọng,\n</p>" +
                    "<p>Thư viện của chúng tôi.</p>";

            Email email = Email.builder()
                    .subject("Thông báo: Trạng thái mượn sách")
                    .content(content)
                    .toUser(user.getName())
                    .toEmail(user.getEmail())
                    .sendTime(new Date())
                    .status(false)
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .build();
            emailList.add(email);
        }
        emailRepository.saveAll(emailList);
    }
}
