package com.example.demo.Controller.khachhang;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panda/")
public class TrangChuController {
    @GetMapping("/trangchu")
    public String hienthi(){
        return "/khachhang/TrangChu";
    }
    @GetMapping("/sanphamchitiet")
    public String sanphamchitiet(){
        return "/khachhang/SPCT";
    }
    @GetMapping("/giohang")
    public String giohang(){
        return "/khachhang/GioHang";

    }
    @GetMapping("/thanhtoan")
    public String thanhtoan(){
        return "/khachhang/ThanhToan";

    }
}
