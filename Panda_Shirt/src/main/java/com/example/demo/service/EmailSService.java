package com.example.demo.service;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.Voucher;
import com.example.demo.respository.VoucherRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailSService {
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    VoucherRepository voucherRepository;
    public void guimail(String toEmail, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom("conbotthoiok@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(mimeMessage);

    }

    public Voucher layvoucher(Integer id){
     return  voucherRepository.findById(id).orElse(null);
    }
}
