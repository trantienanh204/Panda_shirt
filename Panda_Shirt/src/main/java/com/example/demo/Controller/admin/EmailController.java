package com.example.demo.Controller.admin;

import com.example.demo.entity.Customer;
import com.example.demo.entity.EmailRequest;
import com.example.demo.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/sendemails")
    public ResponseEntity<String> sendVoucherEmails(@RequestBody EmailRequest emailRequest) {
        String voucherId = emailRequest.getVoucherId();
        List<Customer> customers = emailRequest.getKhachHangList();

        String subject = "Nhận mã Voucher từ Panda-Shirt nào bạn ơi";
        String MaVC = emailService.layvoucher(Integer.parseInt(voucherId)).getMa();
        String Mota = emailService.layvoucher(Integer.parseInt(voucherId)).getMota();
        String Dk = emailService.layvoucher(Integer.parseInt(voucherId)).getDieuKien();
        String MG = emailService.layvoucher(Integer.parseInt(voucherId)).getMucGiam();
        Boolean Loai = emailService.layvoucher(Integer.parseInt(voucherId)).isLoai();
        LocalDate NBD = emailService.layvoucher(Integer.parseInt(voucherId)).getNgaybatdau();
        LocalDate NKT = emailService.layvoucher(Integer.parseInt(voucherId)).getNgayketthuc();
        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"vi\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Thông báo mã CAPTCHA</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css\"/>"+
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .email-container {\n" +
                "            background-color: #ffffff;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            background-color: #FF8C00;\n" +
                "            color: white;\n" +
                "            padding: 10px;\n" +
                "            border-radius: 8px 8px 0 0;\n" +
                "        }\n" +
                "        .header h1 {\n" +
                "            margin: 0;\n" +
                "            font-size: 24px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .content h2 {\n" +
                "            color: #333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .captcha-code {\n" +
                "            display: inline-block;\n" +
                "            background-color: #f7f7f7;\n" +
                "            font-size: 24px;\n" +
                "            letter-spacing: 2px;\n" +
                "            color: #007bff;\n" +
                "            padding: 10px 20px;\n" +
                "            border: 2px dashed #007bff;\n" +
                "            border-radius: 8px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .content p {\n" +
                "            color: #555;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .noidung{\n" +
                "            font-size: 20px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            font-size: 12px;\n" +
                "            color: #aaa;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"email-container\">\n" +
                "    <div class=\"header\">\n" +
                "        <h1>Ưu đãi từ Panda-Shirt</h1>\n" +
                "    </div>\n" +
                "    <div class=\"content\">\n" +
                "        <h2>Dưới đây là mã giảm giá đặc biệt dành cho bạn</h2>\n" +
                "        <div class=\"captcha-code\">\n" +
                MaVC +
                "        </div>\n" +
                "<div class=\"noidung\">\n" +
                "        <b>Mức giảm: </b>\n" + MG + "" +  (Loai ? " %" : " VND")  +
                "</div>"+
                "<div class=\"noidung\">\n" +
                "        <b>Điều kiện áp dụng: </b>\n" + Dk +""+
                "</div>"+
                "<div class=\"noidung\">\n" +
                "        <b>Mô tả voucher: </b>\n" + Mota +""+
                "</div>"+
                "<div class=\"noidung\">\n" +
                "        <b>Từ ngày:</b>\n"  + NBD +
                "    </div>\n" +
                "<div class=\"noidung\">\n" +
                "        <b>Đến ngày:</b>\n"  + NKT +
                "        <p>Vui lòng nhập mã Voucher này để nhận ưu đãi.</p>\n" +
                "    </div>\n" +
                "</div>"+
                "    <div class=\"footer\">\n" +
                "        <p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.</p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";

        // Gửi email cho từng khách hàng trong danh sách
        for (Customer customer : customers) {
            try {
                emailService.guimail(customer.getTentaikhoan(), subject, body);
            } catch (MessagingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gửi email thất bại cho: " + customer.getTentaikhoan());
            }
        }
        return ResponseEntity.ok("{ \"message\": \"Mã giảm giá đã được gửi thành công!\" }");

    }
}
