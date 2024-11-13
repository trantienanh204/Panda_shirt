package com.example.demo.Controller.khachhang;

import com.example.demo.entity.KichThuoc;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.TrangchuService;
import com.example.demo.service.sanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
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
    sanPhamService sanPhamService;
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
            for (Map<String, Object> item : sizesAndColors) {
                System.out.println("Size: " + ((KichThuoc) item.get("size")).getTen());
                System.out.println("Color: " + ((MauSac) item.get("color")).getTen());
            }
            model.addAttribute("sizesAndColors", sizesAndColors);
        }
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
    @GetMapping("/test")
    @ResponseBody
    public List<SanPhamChiTiet>  sanphamchitiet(@RequestParam Integer id) {
        System.out.println("ID: " + id);
        List<SanPhamChiTiet> result = trangchuService.timkiemspct(id);
        System.out.println("Result: " + result);
        return result;
    }


}
