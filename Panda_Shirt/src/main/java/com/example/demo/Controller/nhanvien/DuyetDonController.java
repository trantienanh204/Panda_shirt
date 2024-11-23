package com.example.demo.Controller.nhanvien;

import com.example.demo.DTO.KhachHangDTO;
import com.example.demo.DTO.NhanVienDTO;
import com.example.demo.DTO.TaiKhoanDTO;
import com.example.demo.entity.*;
import com.example.demo.respository.HoaDonCTRepository;
import com.example.demo.respository.NhanVienRespository;
import com.example.demo.respository.nhanVien.DonHangRepository;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/panda/nhanvien/duyetdon")
public class DuyetDonController {
    @Autowired
    NhanVienRespository nhanVienRespository;
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
    @Autowired
    private TaiKhoanService taiKhoanService;
    @GetMapping("/hienthi")
    public String hienthi(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "mahd", required = false) String mahd,
//                          @RequestParam(value = "tennv", required = false) String tennv,
                          @RequestParam(value = "tenkh", required = false) String tenkh,
                          @RequestParam(value = "Date", required = false)
                              @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate Date,
                          @RequestParam(value = "trangThai", required = false, defaultValue = "Chờ duyệt") String trangThai,
                          Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        if (page < 0) {
            page = 0;
        }

        Page<DonHang> listDH = donHangService.hienThiDH(page, mahd, tenkh,Date, trangThai);

        model.addAttribute("totalPage", listDH.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listcd", listDH.getContent());
        model.addAttribute("mahd", mahd);
//        model.addAttribute("tennv", tennv);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("Date", Date);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageSize", listDH.getSize());
        return "/nhanvien/DuyetDon";
    }

    @GetMapping("/hienthi/daduyet")
    public String hienthi2(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "mahd", required = false) String mahd,
                           @RequestParam(value = "tennv", required = false) String tennv,
                           @RequestParam(value = "tenkh", required = false) String tenkh,
                           @RequestParam(value = "Date", required = false)
                               @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate Date,
                           @RequestParam(value = "trangThai", required = false, defaultValue = "Đã duyệt") String trangThai,
                           Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        if (page < 0) {
            page = 0;
        }


        Page<DonHang> listDH = donHangService.hienThiDH(page, mahd,  tenkh,Date, trangThai);

        model.addAttribute("totalPage", listDH.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listcd", listDH.getContent());
        model.addAttribute("mahd", mahd);
//        model.addAttribute("tennv", tennv);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("Date", Date);
        model.addAttribute("pageSize", listDH.getSize());
        return "/nhanvien/DuyetDon";
    }
    @GetMapping("/hienthi/danggiao")
    public String hienthi4(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "mahd", required = false) String mahd,
                           @RequestParam(value = "tennv", required = false) String tennv,
                           @RequestParam(value = "tenkh", required = false) String tenkh,
                           @RequestParam(value = "Date", required = false)
                               @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate Date,
                           @RequestParam(value = "trangThai", required = false, defaultValue = "Đang giao") String trangThai,
                           Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        if (page < 0) {
            page = 0;
        }


        Page<DonHang> listDH = donHangService.hienThiDH(page, mahd,  tenkh,Date, trangThai);

        model.addAttribute("totalPage", listDH.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listcd", listDH.getContent());
        model.addAttribute("mahd", mahd);
//        model.addAttribute("tennv", tennv);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("Date", Date);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageSize", listDH.getSize());
        return "/nhanvien/DuyetDon";
    }
    @GetMapping("/hienthi/hoanthanh")
    public String hienthi5(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "mahd", required = false) String mahd,
                           @RequestParam(value = "tennv", required = false) String tennv,
                           @RequestParam(value = "tenkh", required = false) String tenkh,
                           @RequestParam(value = "Date", required = false)
                               @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate Date,
                           @RequestParam(value = "trangThai", required = false, defaultValue = "Hoàn thành") String trangThai,
                           Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        if (page < 0) {
            page = 0;
        }


        Page<DonHang> listDH = donHangService.hienThiDH(page, mahd,  tenkh,Date, trangThai);

        model.addAttribute("totalPage", listDH.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listcd", listDH.getContent());
        model.addAttribute("mahd", mahd);
//        model.addAttribute("tennv", tennv);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("Date", Date);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageSize", listDH.getSize());
        return "/nhanvien/DuyetDon";
    }

    @GetMapping("/hienthi/dahuy")
    public String hienthi3(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "mahd", required = false) String mahd,
                           @RequestParam(value = "tennv", required = false) String tennv,
                           @RequestParam(value = "tenkh", required = false) String tenkh,
                           @RequestParam(value = "Date", required = false)
                               @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate Date,
                           @RequestParam(value = "trangThai", required = false, defaultValue = "Đã hủy") String trangThai,
                           Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        if (page < 0) {
            page = 0;
        }

        Page<DonHang> listDH = donHangService.hienThiDH(page, mahd,  tenkh,Date, trangThai);

        model.addAttribute("totalPage", listDH.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listcd", listDH.getContent());
        model.addAttribute("mahd", mahd);
//        model.addAttribute("tennv", tennv);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("Date", Date);
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
    @GetMapping("/update2/{id}")
    public String detailDH2(@PathVariable Integer id, Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("DonHang", new Voucher());

        DonHang donHang = donHangRepository.getReferenceById(id);
        model.addAttribute("DonHang", donHang);
        HoaDonCT hoaDonCT = hdctService.findID(donHang.getHoaDon().getId());
        model.addAttribute("listhdct", hoaDonCT);
        SanPhamChiTiet sanPhamChiTiet = sanPhamService.Listtimkiemspct(hoaDonCT.getSanPhamChiTiet().getId());
        model.addAttribute("listsp", sanPhamChiTiet);
        return "/nhanvien/Update/DuyetDonUpdate2";
    }
    @GetMapping("/update3/{id}")
    public String detailDH3(@PathVariable Integer id, Model model) {
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("DonHang", new Voucher());

        DonHang donHang = donHangRepository.getReferenceById(id);
        model.addAttribute("DonHang", donHang);
        HoaDonCT hoaDonCT = hdctService.findID(donHang.getHoaDon().getId());
        model.addAttribute("listhdct", hoaDonCT);
        SanPhamChiTiet sanPhamChiTiet = sanPhamService.Listtimkiemspct(hoaDonCT.getSanPhamChiTiet().getId());
        model.addAttribute("listsp", sanPhamChiTiet);
        return "/nhanvien/Update/DuyetDonUpdate3";
    }

        @GetMapping("/dongy/{id}")
        public String dongy(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
            DonHang donHang = donHangRepository.getReferenceById(id);

            donHang.setTrangThai("Đã duyệt");
            String username = userDetails.getUsername();
            TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(username);
            if (taiKhoanDto == null || taiKhoanDto.getNhanVienDTO() == null) {
                return "redirect:/panda/login";
            }
            NhanVien nhanVien = mapToNhanvien(taiKhoanDto.getNhanVienDTO());
            donHang.setNhanVien(nhanVien);
            donHangRepository.save(donHang);

            return "redirect:/panda/nhanvien/duyetdon/hienthi";
        }

        private NhanVien mapToNhanvien(NhanVienDTO dto) {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setId(dto.getId());
            nhanVien.setManhanvien(dto.getManhanvien());
            nhanVien.setTennhanvien(dto.getTennhanvien());
            return nhanVien;
        }



    @GetMapping("/xacnhan/{id}")
    public String xacnhan(@PathVariable("id") Integer id) {
        DonHang donHang = donHangRepository.getReferenceById(id);
        donHang.setTrangThai("Đang giao");
        donHangRepository.save(donHang);
            return "redirect:/panda/nhanvien/duyetdon/hienthi";
    }
  @GetMapping("/dagiao/{id}")
    public String dagiao(@PathVariable("id") Integer id,@AuthenticationPrincipal UserDetails userDetails) {
        DonHang donHang = donHangRepository.getReferenceById(id);
      HoaDon hoaDon = hoaDonService.findById(donHang.getHoaDon().getId());
      String username = userDetails.getUsername();
      TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(username);
      if (taiKhoanDto == null || taiKhoanDto.getNhanVienDTO() == null) {
          return "redirect:/panda/login";
      }
      NhanVien nhanVien = mapToNhanvien(taiKhoanDto.getNhanVienDTO());
        donHang.setTrangThai("Hoàn thành");
      donHang.setNhanVien(nhanVien);
      hoaDon.setNhanVien(nhanVien);
      donHangRepository.save(donHang);
      hoaDonService.save(hoaDon);

            return "redirect:/panda/nhanvien/duyetdon/hienthi";
    }

    @PostMapping("/tuchoi")
    public String tuchoi(@RequestParam("lydohuy") String lydohuy, @RequestParam("id") Integer id) {
        DonHang donHang = donHangRepository.getReferenceById(id);
        HoaDonCT hoaDonCT = hdctService.findID(donHang.getHoaDon().getId());
        SanPhamChiTiet sanPhamChiTiet = sanPhamService.Listtimkiemspct(hoaDonCT.getSanPhamChiTiet().getId());
        // Cập nhật số lượng có sẵn trong kho
        int soLuongChiTiet = sanPhamChiTiet.getSoluongsanpham(); // Số lượng sản phẩm chi tiết
        int soLuongDat = hoaDonCT.getSoluong(); // Số lượng đã đặt

        // Cộng số lượng đã đặt vào số lượng có sẵn trong kho
        sanPhamChiTiet.setSoluongsanpham(soLuongChiTiet + soLuongDat);

        // Lưu lại thông tin cập nhật
        sanPhamService.saveSanPhamChiTiet(sanPhamChiTiet);
        // Cập nhật trạng thái đơn hàng thành "Đã hủy"
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
