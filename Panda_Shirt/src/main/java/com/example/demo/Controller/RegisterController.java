package com.example.demo.Controller;

import com.example.demo.entity.KhachHang;
import com.example.demo.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/panda")
public class RegisterController {
    @Autowired
    RegisterService resgisterService;
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("khachHang",new KhachHang());
        return "Register"; // Trả về tên view
    }

    @PostMapping("/register/create")
    public String createAcount(@Validated @ModelAttribute KhachHang khachHang, BindingResult result, RedirectAttributes redirectAttributes) { // Thêm @ModelAttribute
        if (result.hasErrors()) {
            return "Register";
        }
        resgisterService.createAcount(khachHang);
        redirectAttributes.addFlashAttribute("message", "Bạn đã đăng ký thành công!");
        return "redirect:/panda/login"; // Redirect đến login
    }
}
