package com.example.demo.Controller.khachhang;

import com.example.demo.DTO.TaiKhoanDTO;
import com.example.demo.entity.*;
import com.example.demo.service.GioHangService;
import com.example.demo.service.SanPhamService;
import com.example.demo.service.TaiKhoanService;
import com.example.demo.service.TrangchuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/panda/")
public class TrangChuController {
    @Autowired
    TrangchuService trangchuService;
    @Autowired
    SanPhamService sanPhamService;
    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @GetMapping("/trangchu")
    public String hienthi(Model model){
        model.addAttribute("sanpham",trangchuService.spfinall());
        return "/khachhang/TrangChu";
    }
    @GetMapping("/sanphamchitiet/{id}")
    public String sanphamchitiet(Model model, @PathVariable("id") Integer id) {
        Optional<SanPham> sanPham = sanPhamService.findSanPhamById(id);
        if (sanPham.isPresent()) {
            model.addAttribute("sanPham", sanPham.get());

            // Lấy giá trị min và max
            Double[] minMaxPrice = sanPhamService.getMinMaxPrice(id);
            model.addAttribute("minPrice", minMaxPrice[0]);
            model.addAttribute("maxPrice", minMaxPrice[1]);

            // Lấy danh sách màu sắc
            List<MauSac> colors = sanPhamService.getColors(id);
            model.addAttribute("colors", colors);

            // Lấy danh sách kích thước
            List<Map<String, Object>> sizesAndColors = sanPhamService.getSizesAndColors(id);
            model.addAttribute("sizesAndColors", sizesAndColors);
        }
        return "/khachhang/SPCT";
    }



    @GetMapping("/giohang")
    public String giohang(
            Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String tenDangNhap = userDetails.getUsername();
        TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);
        if (taiKhoanDto == null || taiKhoanDto.getKhachHangDTO() == null) {
            return "redirect:/panda/login"; }
        int khachHangId = taiKhoanDto.getKhachHangDTO().getId();
        List<GioHang> cartItems = gioHangService.getCartItems(khachHangId);
        model.addAttribute("cartItems", cartItems);

        return "/khachhang/GioHang";
    }
    @GetMapping("/taikhoan")
    public String taikhoan(){
        return "/khachhang/TaiKhoan";
    }
    @GetMapping("/thanhtoan")
    public String thanhtoan(){
        return "/khachhang/ThanhToan";

    }
    @GetMapping("/test")
    @ResponseBody
    public List<SanPhamChiTiet>  sanphamchitiet(@RequestParam Integer id) {
        System.out.println("ID: " + id);
        List<SanPhamChiTiet> result = trangchuService.timkiemspct(id);
        System.out.println("Result: " + result);
        return result;
    }


}
