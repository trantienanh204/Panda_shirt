package com.example.demo.Controller.admin.QLSP;

import com.example.demo.Repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panda/mausac")
public class MauSacController {
    @Autowired
    MauSacRepository mauSacRepository;

    @GetMapping()
    public String mausac(Model model) {
        String role = "admin";
        model.addAttribute("role", role);
        model.addAttribute("lsmausac", mauSacRepository.findAll());
        return "admin/QLSP/MauSac";
    }
}
