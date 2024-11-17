package com.example.demo.Controller;

import com.example.demo.entity.ChatLieu;
import com.example.demo.services.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/panda")
public class SideBarController {
    @Autowired
    ChatLieuService chatLieuService;
    //  Trả về trang thống kê khi hiển thị
    @GetMapping("/hienthi")
    public String hienthi(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role); // Truyền role xuống view
        return "/admin/QLSP/SanPham";
    }
//    khi chưa code back-end thì chỉ có phần font-end
//    ta trả về một giao diện bth
//    nhưng nếu đã code back-end bên controller rồi
//    hì phải trả về theo kiểu như này:
//    "redirect:/thongke/hienthi" (Xem lại dự án xưởng nếu quên)

    //    QLSP===================
    @GetMapping("/sanpham")
    public String sanpham(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/QLSP/SanPham";
    }
    @GetMapping("/ctsp")
    public String ctsp(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/QLSP/CTSP";
    }
    @GetMapping("/mausac")
    public String mausac(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/QLSP/MauSac";
    }
    @GetMapping("/kichthuoc")
    public String kichthuoc(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/QLSP/KichThuoc";
    }

    @GetMapping("/danhmuc")
    public String danhmuc(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/QLSP/DanhMuc";
    }
    @GetMapping("/nsx")
    public String nsx(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/QLSP/NSX";
    } @GetMapping("/thuonghieu")
    public String thuonghieu(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/QLSP/ThuongHieu";
    }

    //          QLGG ====================
    @GetMapping("/giamgia")
    public String giamgia(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/GiamGia";
    }
    //      QLTK ===================


    @GetMapping("/hoadon")
    public String hoadon(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/HoaDon/HoaDon";
    }
    @GetMapping("/hdct")
    public String hdct(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/HoaDon/HDCT";
    }

    //========================
    //Bán Hàng
    @GetMapping("/nhanvien/banhang")
    public String banhang(Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/nhanvien/BanHang";
    }
    @GetMapping("/nhanvien/duyetdon")
    public String duyetdon(Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/nhanvien/DuyetDon";
    }
    @GetMapping("/nhanvien/xemsanpham")
    public String xemsp(Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/nhanvien/XemSanPham";
    }
    @GetMapping("/nhanvien/xemctsp")
    public String xemctsp(Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/nhanvien/XemCTSP";
    }
}
