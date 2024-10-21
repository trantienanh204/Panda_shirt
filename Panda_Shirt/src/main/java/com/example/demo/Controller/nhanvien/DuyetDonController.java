package com.example.demo.Controller.nhanvien;

import com.example.demo.respository.nhanVien.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panda/nhanvien/duyetdon")
public class DuyetDonController {
    @Autowired
    DonHangRepository donHangRepository;

    @GetMapping("/hienthi")
    public String hienthi(Model model){
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("listcd",donHangRepository.listChoDuyet());
        return "/nhanvien/DuyetDon";
    }
    @GetMapping("/hienthi/daduyet")
    public String hienthi2(Model model){
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("listcd",donHangRepository.listDaDuyet());
        return "/nhanvien/DuyetDon";
    }
    @GetMapping("/hienthi/dahuy")
    public String hienthi3(Model model){
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("listcd",donHangRepository.listDaHuy());
        return "/nhanvien/DuyetDon";
    }
}
