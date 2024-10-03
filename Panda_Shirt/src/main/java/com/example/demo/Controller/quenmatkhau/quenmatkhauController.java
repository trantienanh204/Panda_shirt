package com.example.demo.Controller.quenmatkhau;


    import com.example.demo.entity.NhanVien;
    import com.example.demo.service.QuenmatkhauService;
    import jakarta.mail.MessagingException;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("quenmatkhau")
    public class quenmatkhauController {

        @Autowired
        private QuenmatkhauService quenmatkhauService;
@GetMapping("/timtaikhoan")
public String timtaikhoan (){
    return "quenmatkhau/QuenMatKhai";
}
    @PostMapping("/guimail")
    public String guimail(@RequestParam("tentaikhoan") String tentaikhoan,HttpSession session, Model model) {
        String regexEmail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (tentaikhoan == null || tentaikhoan.isEmpty() || !tentaikhoan.matches(regexEmail)) {
            model.addAttribute("errorMessage", "Tên tài khoản không hợp lệ. Vui lòng nhập địa chỉ email hợp lệ.");
            return "quenmatkhau/QuenMatKhai";
        }

        NhanVien nhanVien = quenmatkhauService.timkiemnhanvien(tentaikhoan);

        if (nhanVien != null) {
            String toEmail = nhanVien.getTentaikhoan();
            if (quenmatkhauService.sendEmail(toEmail)) {
                session.setAttribute("email", toEmail);
                return "quenmatkhau/nhapcapcha";
            } else {
                model.addAttribute("errorMessage", "Gửi email thất bại. Vui lòng thử lại.");
                return "quenmatkhau/QuenMatKhai";
            }
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy tài khoản với thông tin đã nhập.");
            return "quenmatkhau/QuenMatKhai";
        }
    }


    @PostMapping("/xacnhan")
    public String xacNhanOTP(@RequestParam("OTP") String OTP, HttpSession session, Model model) {
    //ờmmmmmmmmmmmmmmmmmmmmmmmmmmm
        String email = (String) session.getAttribute("email");

        String maCaptchaDaGui = quenmatkhauService.getCaptchaForEmail(email);

        if (maCaptchaDaGui != null && maCaptchaDaGui.equals(OTP)) {
            model.addAttribute("successMessage", "Xác nhận thành công!");
            return "quenmatkhau/DoiMatKhau";
        } else {
            model.addAttribute("errorMessage", "Mã CAPTCHA không chính xác. Vui lòng thử lại.");
            return "quenmatkhau/nhapcapcha";
        }
    }

    @PostMapping("/doimatkhau")
    public String doiMatKhau(@RequestParam("matkhaumoi") String matkhaumoi,
                             @RequestParam("matkhauXacNhan") String matkhauXacNhan,
                             HttpSession session, Model model) {

        String email = (String) session.getAttribute("email");
        NhanVien nhanVien = quenmatkhauService.timkiemnhanvien(email);

        if (nhanVien != null) {
            if (matkhaumoi.equals(matkhauXacNhan)) {
                nhanVien.setMatkhau(matkhaumoi);
                quenmatkhauService.themnhanvien(nhanVien);

                model.addAttribute("successMessage", "Đổi mật khẩu thành công!");
                return "Login";
            } else {
                model.addAttribute("errorMessage", "Mật khẩu xác nhận không khớp. Vui lòng thử lại.");
                return "quenmatkhau/DoiMatKhau";
            }
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy tài khoản.");
            return "quenmatkhau/DoiMatKhau";
        }
    }
    @PostMapping("/guiLaiMa")
    public String guiLaiMa(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email != null) {
            boolean sent = quenmatkhauService.sendEmail(email);
            if (sent) {
                String otp = quenmatkhauService.getCaptchaForEmail(email);
                quenmatkhauService.updateCaptchaForEmail(email, otp);
                model.addAttribute("successMessage", "Mã OTP mới đã được gửi đến email của bạn.");
            } else {
                model.addAttribute("errorMessage", "Gửi lại mã OTP thất bại. Vui lòng thử lại.");
            }
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy email để gửi mã.");
        }
        return "quenmatkhau/nhapcapcha";
    }
    @ResponseBody
 @PostMapping("/listOTPEmail")
    public String listOTPEmail(@RequestParam("email")String email){
    return quenmatkhauService.getCaptchaForEmail(email);
 }






    //quenmatkhauService.macapchaList.add(Integer.parseInt(captchaCode));
    }


