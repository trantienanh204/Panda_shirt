package com.example.demo.Controller.nhanvien;

import com.example.demo.entity.*;
import com.example.demo.respository.HoaDonCTRepository;
import com.example.demo.respository.nhanVien.DonHangRepository;
import com.example.demo.service.DonHangService;
import com.example.demo.service.HDCTService;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/panda/nhanvien/duyetdon")
public class DuyetDonController {
    @Autowired
    DonHangRepository donHangRepository;
    @Autowired
    HoaDonCTRepository hoaDonCTRepository;
    @Autowired
    DonHangService donHangService;
    @Autowired
    HDCTService hdctService;
    @Autowired
    SanPhamService sanPhamService;
    @Autowired
    HoaDonService hoaDonService;
    @GetMapping("/hienthi")
    public String hienthi(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "mahd", required = false) String mahd,
                          @RequestParam(value = "tennv", required = false) String tennv,
                          @RequestParam(value = "tenkh", required = false) String tenkh,
                          @RequestParam(value = "trangThai", required = false, defaultValue = "Chờ duyệt") String trangThai,
                          Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        if (page < 0) {
            page = 0;
        }
        Page<DonHang> listDH = donHangService.hienThiDH(page, mahd, tennv, tenkh, trangThai);
        model.addAttribute("totalPage", listDH.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listcd", listDH.getContent());
        model.addAttribute("mahd", mahd);
        model.addAttribute("tennv", tennv);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageSize", listDH.getSize());
        return "/nhanvien/DuyetDon";
    }

    @GetMapping("/hienthi/daduyet")
    public String hienthi2(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "mahd", required = false) String mahd,
                           @RequestParam(value = "tennv", required = false) String tennv,
                           @RequestParam(value = "tenkh", required = false) String tenkh,
                           @RequestParam(value = "trangThai", required = false, defaultValue = "Đã duyệt") String trangThai,
                           Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        if (page < 0) {
            page = 0;
        }

        Page<DonHang> listDH = donHangService.hienThiDH(page, mahd, tennv, tenkh, trangThai);
        model.addAttribute("totalPage", listDH.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listcd", listDH.getContent());
        model.addAttribute("mahd", mahd);
        model.addAttribute("tennv", tennv);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageSize", listDH.getSize());
        return "/nhanvien/DuyetDon";
    }

    @GetMapping("/hienthi/dahuy")
    public String hienthi3(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "mahd", required = false) String mahd,
                           @RequestParam(value = "tennv", required = false) String tennv,
                           @RequestParam(value = "tenkh", required = false) String tenkh,
                           @RequestParam(value = "trangThai", required = false, defaultValue = "Đã hủy") String trangThai,
                           Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        if (page < 0) {
            page = 0;
        }
        Page<DonHang> listDH = donHangService.hienThiDH(page, mahd, tennv, tenkh, trangThai);
        model.addAttribute("totalPage", listDH.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listcd", listDH.getContent());
        model.addAttribute("mahd", mahd);
        model.addAttribute("tennv", tennv);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageSize", listDH.getSize());
        return "/nhanvien/DuyetDon";
    }

    @GetMapping("/update/{id}")
    public String detailDH(@PathVariable Integer id, Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("DonHang", new Voucher());

        DonHang donHang = donHangRepository.getReferenceById(id);
        model.addAttribute("DonHang", donHang);
        HoaDonCT hoaDonCT = hdctService.findID(donHang.getHoaDon().getId());
        model.addAttribute("listhdct", hoaDonCT);
        SanPhamChiTiet sanPhamChiTiet = sanPhamService.Listtimkiemspct(hoaDonCT.getSanPhamChiTiet().getId());
        model.addAttribute("listsp", sanPhamChiTiet);
        return "/nhanvien/Update/DuyetDonUpdate";
    }

    @GetMapping("/dongy/{id}")
    public String dongy(@PathVariable("id") Integer id) {
        DonHang donHang = donHangRepository.getReferenceById(id);
        donHang.setTrangThai("Đã duyệt");
        donHangRepository.save(donHang);
            return "redirect:/panda/nhanvien/duyetdon/hienthi";
    }

    @PostMapping("/tuchoi")
    public String tuchoi(@RequestParam("lydohuy") String lydohuy, @RequestParam("id") Integer id) {
        DonHang donHang = donHangRepository.getReferenceById(id);
        donHang.setLydohuy(lydohuy);
        donHang.setTrangThai("Đã hủy");
        donHangRepository.save(donHang);
        return "redirect:/panda/nhanvien/duyetdon/hienthi";
    }
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("DonHang", new Voucher());

        DonHang donHang = donHangRepository.getReferenceById(id);
        model.addAttribute("DonHang", donHang);
        HoaDonCT hoaDonCT = hdctService.findID(donHang.getHoaDon().getId());
        model.addAttribute("listhdct", hoaDonCT);
        SanPhamChiTiet sanPhamChiTiet = sanPhamService.Listtimkiemspct(hoaDonCT.getSanPhamChiTiet().getId());
        model.addAttribute("listsp", sanPhamChiTiet);
        return "/nhanvien/Update/DuyetDonDetal";
    }
}
