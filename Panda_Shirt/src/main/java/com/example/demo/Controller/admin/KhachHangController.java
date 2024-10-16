package com.example.demo.Controller.admin;

import com.example.demo.respository.KhachHangRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/panda/tkkhachhang")
public class KhachHangController {
    @Autowired
    KhachHangRespository khachHangRespository;

    @GetMapping("/hienthi")
    public String hienthi(Model model){
        model.addAttribute("listkh", khachHangRespository.findAll());
        return "/admin/GiamGia";
    }
}
