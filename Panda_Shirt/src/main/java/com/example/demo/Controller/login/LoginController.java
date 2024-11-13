package com.example.demo.Controller.login;

import com.example.demo.DTO.KhachHangDTO;
import com.example.demo.DTO.TaiKhoanDTO;
import com.example.demo.entity.NhanVien;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.respository.NhanVienRespository;
import com.example.demo.respository.TaiKhoanRepo;
import com.example.demo.service.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/panda")
public class LoginController {
    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    NhanVienRespository nhanVienRepository;

    @Autowired
    TaiKhoanRepo taiKhoan ;

    NhanVien nv = new NhanVien();

    @GetMapping("/checkRoles")
    public String checkRoles(Authentication authentication) {
        encodeAllPasswords();
        return "ok";
    }

    @GetMapping("/mahoa")
    @Transactional
    public void encodeAllPasswords() {
        List<TaiKhoan> tkls = taiKhoan.findAll();
        for (TaiKhoan tk : tkls) {
            // Kiểm tra nếu mật khẩu chưa được mã hóa
            String plainPassword = tk.getMatKhau();
            if (!passwordEncoder.matches(plainPassword, plainPassword)) {
                String encodedPassword = passwordEncoder.encode(plainPassword);
                tk.setMatKhau(encodedPassword);
                taiKhoan.save(tk);
            }
        }
    }

//    @GetMapping("/account")
//    public String login(@RequestParam("username") String username,
//                        @RequestParam("password") String password,
//                        Model model, HttpSession session) {
//        System.out.println("dsafdsfd");
//
//        String regex = "^[a-zA-Z0-9@._]*$";
//        if (!username.matches(regex) || !password.matches(regex)) {
//            model.addAttribute("saitk", "Tài khoản hoặc mật khẩu sai định dạng");
//            return "Login";
//        }
//
//        model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
//        return "login";  // Trả về lại trang đăng nhập nếu đăng nhập thất bại
//    }


    @GetMapping("/login")
    public String account() {
        return "Login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute TaiKhoanDTO taiKhoanDTO, Model model, HttpSession session) {
        String tenDangNhap = taiKhoanDTO.getTenDangNhap();
        String matKhau = taiKhoanDTO.getMatKhau();
        // Tìm tài khoản
        TaiKhoanDTO foundTaiKhoan = taiKhoanService.findByTenDangNhap(tenDangNhap);

        if (foundTaiKhoan == null) {
            model.addAttribute("error", "Tên đăng nhập không tồn tại.");
            return "redirect:/panda/login";
        }

        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(matKhau, foundTaiKhoan.getMatKhau())) {
            model.addAttribute("error", "Mật khẩu không chính xác.");
            return "redirect:/panda/login";
        }
        // Lấy thông tin khách hàng
        KhachHangDTO khachHangDTO = foundTaiKhoan.getKhachHangDTO();
        if (khachHangDTO == null) {
            model.addAttribute("error", "Không tìm thấy thông tin khách hàng.");
            return "redirect:/panda/login";

        }
        // Lưu thông tin người dùng vào session
//        session.setAttribute("loggedInUser", khachHangDTO);

        // Lấy roomId từ session
//        Integer roomId = (Integer) session.getAttribute("roomId");

        // Chuyển hướng đến trang chi tiết phòng nếu roomId có
//        if (roomId != null) {
//            return "redirect:/showRoomDetailPhong?roomId=" + roomId;
//        }
        return "redirect:/"; // Trở về trang chính
    }


}
