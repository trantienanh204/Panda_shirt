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

import java.util.*;
import java.util.stream.Collectors;

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
        List<SanPham> sanphamList = trangchuService.spfinall();
        List<Map<String, Object>> processedSanPhamList = new ArrayList<>();

        for (SanPham sp : sanphamList) {
            Map<String, Object> spMap = new HashMap<>();
            spMap.put("id", sp.getId());
            spMap.put("masp", sp.getMasp());
            spMap.put("tensp", sp.getTensp());
            spMap.put("minPrice", sp.getMinPrice());

            if (sp.getAnhsp() != null) {
                // Chuyển đổi dữ liệu byte array thành chuỗi base64
                String base64Image = Base64.getEncoder().encodeToString(sp.getAnhsp());
                spMap.put("anhsp", base64Image);
            } else {
                spMap.put("anhsp", null);
            }

            processedSanPhamList.add(spMap);
        }

        model.addAttribute("sanpham", processedSanPhamList);
        return "/khachhang/TrangChu";
    }


    @GetMapping("/sanphamchitiet/{id}")
    public String sanphamchitiet(Model model, @PathVariable("id") Integer id) {
        Optional<SanPham> sanPham = sanPhamService.findSanPhamById(id);
        if (sanPham.isPresent()) {
            SanPham sp = sanPham.get();

            // Chuyển đổi dữ liệu byte array thành chuỗi base64 nếu không null
            if (sp.getAnhsp() != null) {
                String base64Image = Base64.getEncoder().encodeToString(sp.getAnhsp());
                model.addAttribute("anhspBase64", base64Image); // Truyền dữ liệu base64 vào model
            } else {
                model.addAttribute("anhspBase64", null);
            }

            // Log số lượng sản phẩm chi tiết
            for (SanPhamChiTiet chiTiet : sp.getSanPhamChiTietList()) {
                System.out.println("Sản phẩm chi tiết ID: " + chiTiet.getId() + ", Số lượng: " + chiTiet.getSoluongsanpham());
            }

            model.addAttribute("sanPham", sp);

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
    public String giohang(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String tenDangNhap = userDetails.getUsername();
        TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);
        if (taiKhoanDto == null || taiKhoanDto.getKhachHangDTO() == null) {
            return "redirect:/panda/login";
        }
        int khachHangId = taiKhoanDto.getKhachHangDTO().getId();
        List<GioHang> cartItems = gioHangService.getCartItems(khachHangId);

        // Chuyển đổi dữ liệu byte array thành chuỗi base64
        List<Map<String, Object>> processedCartItems = cartItems.stream().map(item -> {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("id", item.getId());
            itemMap.put("sanPhamChiTiet", item.getSanPhamChiTiet());
            itemMap.put("soluong", item.getSoluong());

            if (item.getSanPhamChiTiet().getAnhSanPhamChiTiet() != null) {
                String base64Image = Base64.getEncoder().encodeToString(item.getSanPhamChiTiet().getSanPham().getAnhsp());
                itemMap.put("anhspBase64", base64Image);
            } else {
                itemMap.put("anhspBase64", null);
            }

            return itemMap;
        }).collect(Collectors.toList());

        model.addAttribute("cartItems", processedCartItems);
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
