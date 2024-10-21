package com.example.demo.Controller.admin.QLTK;

import com.example.demo.entity.KhachHang;
import com.example.demo.respository.KhachHangRepository;

import com.example.demo.service.EmailService;
import com.example.demo.services.KhachHangService;
import jakarta.validation.Valid;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/panda")
public class TKKhachHangController {
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    KhachHangRepository khachHangRepository;
    @Autowired
    EmailService emailService;
    @GetMapping("/tkkhachhang")
    public String khachhang(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("list",khachHangRepository.findAll());
        return "/admin/QLTK/TKKhachHang";
    }
    @GetMapping("/tkkhachhang/save")
    public String showFormAdd(Model model){
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("khachHang",new KhachHang());
        return "admin/QLTK/ADD/AddTKKhachHang";
    }
    // save
    @PostMapping("/tkkhachhang/save")
    public String save(Model model,
                       @Valid @ModelAttribute KhachHang khachHang,
                       BindingResult result,
                       @RequestParam("file") MultipartFile file,
                       RedirectAttributes redirectAttributes) {
        String role = "admin"; // Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);

        if (result.hasErrors()) {
            return "admin/QLTK/ADD/AddTKKhachHang";
        }

        // Kiểm tra mã và số điện thoại đã tồn tại
        boolean maExists = khachHangRepository.existsKhachHangByMakhachhang(khachHang.getMakhachhang());
        boolean sdtExists = khachHangRepository.existsKhachHangBySdt(khachHang.getSdt());
            if (maExists) {
                model.addAttribute("errorma", "Mã đã tồn tại");
                return "admin/QLTK/ADD/AddTKKhachHang";
            }
            if (sdtExists) {
                model.addAttribute("errorsdt", "Số điện thoại đã tồn tại");
                return "admin/QLTK/ADD/AddTKKhachHang";
            }
        try {
            // Tạo mật khẩu ngẫu nhiên
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
            int passwordLength = 8 + new SecureRandom().nextInt(7); // 8 đến 14
            SecureRandom random = new SecureRandom();
            StringBuilder password = new StringBuilder(passwordLength);

            for (int i = 0; i < passwordLength; i++) {
                int index = random.nextInt(characters.length());
                password.append(characters.charAt(index));
            }

            // Lưu mật khẩu chưa băm để gửi qua email
            String plainPassword = password.toString();
            // Băm mật khẩu
            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

            // Lưu mật khẩu đã băm vào đối tượng KhachHang
            khachHang.setMatkhau(hashedPassword);

            // Gửi email thông báo
            String subject = "Chào mừng khách hàng mới " + khachHang.getTenkhachhang() + " đã tạo tài khoản tại shop Panda Shirt!";
            String body = "<!DOCTYPE html>" +
                    "<html lang='vi'>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                    ".container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }" +
                    "h2 { text-align: center; color: #333; }" +
                    "p { color: #555; line-height: 1.6; }" +
                    ".password { font-size: 24px; font-weight: bold; color: #007bff; text-align: center; margin: 20px 0; padding: 10px; border: 1px solid #007bff; border-radius: 5px; background-color: #e7f3ff; }" +
                    ".footer { margin-top: 20px; text-align: center; color: #555; font-size: 12px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<h2>Chào mừng bạn mở tài khoản tại web bán áo phông Panda Shirt!</h2>" +
                    "<p>Mật khẩu của bạn đã được tạo thành công. Vui lòng ghi nhớ nó!</p>" +
                    "<div class='password'>" + plainPassword + "</div>" + // Gửi mật khẩu chưa băm
                    "<p>Bạn có thể đăng nhập vào tài khoản của mình tại <a href='https://your-shop-url.com' style='color: #007bff;'>đây</a>.</p>" +
                    "<div class='footer'>Nếu bạn có bất kỳ câu hỏi nào, hãy liên hệ với bộ phận hỗ trợ của chúng tôi.</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            // Gửi email
            emailService.sendEmail(khachHang.getTentaikhoan(), subject, body);

            // Set trạng thái và ngày tạo
            khachHang.setTrangthai(1);
            khachHang.setNgaytao(LocalDate.now());

            // Lưu khách hàng vào cơ sở dữ liệu
            khachHangService.saveCustomerToDb(file, khachHang);
            redirectAttributes.addFlashAttribute("saveMassage", "Thêm khách hàng thành công");
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }

        return "redirect:/panda/tkkhachhang";
    }


    // hiển thị hình ảnh
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImages(@PathVariable Integer id) {
        Optional<KhachHang> khachHang = Optional.ofNullable(khachHangService.findById(id));
        if (khachHang.isPresent()) {
            byte[] image = khachHang.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(image.length);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // thay đổi trạng thái
    @GetMapping("/tkkhachhang/change")
    public String changeStatus(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes) {
        String role = "admin"; // Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);

        KhachHang khachHang = khachHangService.findById(id);
        if (khachHang != null) {
            khachHang.toggleTrangThai(); // Đảo ngược trạng thái
            khachHangRepository.save(khachHang);
        }
        redirectAttributes.addFlashAttribute("ChangesStatusMessage", "Chuyển trạng thái thành công !");
        return "redirect:/panda/tkkhachhang";
    }
}
