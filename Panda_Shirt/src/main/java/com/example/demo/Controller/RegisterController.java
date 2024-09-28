package com.example.demo.Controller;

import com.example.demo.entity.KhachHang;
import com.example.demo.respository.ResgisterRespository;
import com.example.demo.services.ResgisterService; // Sửa lỗi chính tả
import org.springframework.beans.factory.annotation.Autowired; // Thêm import
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.spi.RegisterableService;

@Controller
@RequestMapping("/panda")
public class RegisterController {
    @Autowired
    ResgisterService resgisterService;
    @GetMapping("/register")
    public String register() {
        return "Register"; // Trả về tên view
    }

    @PostMapping("/resgister/create") // Đường dẫn đã sửa
    public String createAcount(KhachHang khachHang, RedirectAttributes redirectAttributes) { // Thêm @ModelAttribute
        resgisterService.add(khachHang);
        redirectAttributes.addFlashAttribute("message", "Bạn đã đăng ký thành công!");
        return "redirect:/panda/login"; // Redirect đến login
    }
}
