package com.example.demo.Controller.giohang;

import com.example.demo.Controller.login.UserUtils;
import com.example.demo.entity.*;
import com.example.demo.DTO.KhachHangDTO;
import com.example.demo.respository.nhanVien.DonHangRepository;
import com.example.demo.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.DTO.TaiKhoanDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/panda/giohang")
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private DonHangRepository donHangRepository;


    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestParam int sanPhamChiTietId,
                                            @RequestParam int quantity) {
        try {
            String tenDangNhap = userDetails.getUsername();
            TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);

            if (taiKhoanDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy tài khoản.");
            }

            if (taiKhoanDto.getKhachHangDTO() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy thông tin khách hàng.");
            }

            int khachHangId = taiKhoanDto.getKhachHangDTO().getId();
            System.out.println("sanPhamChiTietId: " + sanPhamChiTietId);
            System.out.println("quantity: " + quantity);
            gioHangService.addToCart(khachHangId, sanPhamChiTietId, quantity);
            return ResponseEntity.ok("Sản phẩm đã được thêm vào giỏ hàng.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    @ResponseBody
    public String getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            System.out.println("UserDetails: " + userDetails);
            System.out.println("Authorities: " + userDetails.getAuthorities());
            String tenDangNhap = userDetails.getUsername();
            TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);
            return "Username: " + taiKhoanDto;
        } else {
            System.out.println("No user is currently logged in.");
            return "No user is currently logged in.";
        }
    }

    @GetMapping("/findSanPhamChiTietId")
    public ResponseEntity<Integer> findSanPhamChiTietId(@RequestParam Integer sizeId, @RequestParam Integer colorId) {
        try {
            int sanPhamChiTietId = sanPhamService.findIdBySizeAndColorId(sizeId, colorId);
            return ResponseEntity.ok(sanPhamChiTietId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@RequestBody Map<String, Object> payload
//            ,
//                                                 @AuthenticationPrincipal UserDetails userDetails
    ) {
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
//        }

        try {
            int sanPhamChiTietId = (int) payload.get("sanPhamChiTietId");
            int quantity = (int) payload.get("quantity");

            System.out.println("Received updateQuantity request for: " + sanPhamChiTietId + " quantity: " + quantity);

            String tenDangNhap = "taikhoan2";
            TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);
            if (taiKhoanDto == null || taiKhoanDto.getKhachHangDTO() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy tài khoản.");
            }
            int khachHangId = taiKhoanDto.getKhachHangDTO().getId();

            // Kiểm tra sản phẩm chi tiết trong giỏ hàng
            List<GioHang> cartItems = gioHangService.getCartItems(khachHangId);
            boolean productFound = cartItems.stream().anyMatch(item -> item.getSanPhamChiTiet().getId() == sanPhamChiTietId);

            if (!productFound) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in the cart.");
            }

            gioHangService.updateQuantity(khachHangId, sanPhamChiTietId, quantity);
            return ResponseEntity.ok("Số lượng sản phẩm đã được cập nhật.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteFromCart(@RequestBody Map<String, Object> payload,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        try {
            int sanPhamChiTietId = (int) payload.get("sanPhamChiTietId");

            System.out.println("Received delete request for product: " + sanPhamChiTietId);

            String tenDangNhap = userDetails.getUsername();
            TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);
            if (taiKhoanDto == null || taiKhoanDto.getKhachHangDTO() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy tài khoản.");
            }
            int khachHangId = taiKhoanDto.getKhachHangDTO().getId();

            // Kiểm tra sản phẩm chi tiết trong giỏ hàng
            List<GioHang> cartItems = gioHangService.getCartItems(khachHangId);
            boolean productFound = cartItems.stream().anyMatch(item -> item.getSanPhamChiTiet().getId() == sanPhamChiTietId);

            if (!productFound) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in the cart.");
            }

            gioHangService.deleteFromCart(khachHangId, sanPhamChiTietId);
            return ResponseEntity.ok("Sản phẩm đã được xóa khỏi giỏ hàng.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }




    @GetMapping("/thanhtoan")
    public String thanhToan(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String tenDangNhap = userDetails.getUsername();
        TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);
        if (taiKhoanDto == null || taiKhoanDto.getKhachHangDTO() == null) {
            return "redirect:/login";
        }

        KhachHangDTO khachHangDto = taiKhoanDto.getKhachHangDTO();
        if (khachHangDto == null) {
            model.addAttribute("message", "Không tìm thấy thông tin khách hàng.");
            return "khachhang/ThongBaoLoi";
        }

        KhachHang khachHang = new KhachHang();
        khachHang.setId(khachHangDto.getId());
        khachHang.setMakhachhang(khachHangDto.getMakhachhang());
        khachHang.setTenkhachhang(khachHangDto.getTenkhachhang());
        khachHang.setSdt(khachHangDto.getSdt());
        khachHang.setDiachi(khachHangDto.getDiachi());

        int khachHangId = khachHang.getId();
        List<GioHang> cartItems = gioHangService.getCartItems(khachHangId);

        if (cartItems.isEmpty()) {
            model.addAttribute("message", "Giỏ hàng của bạn đang trống.");
            return "khachhang/GioHang";
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("khachHang", khachHang);

        double totalAmount = cartItems.stream().mapToDouble(item -> item.getSanPhamChiTiet().getDongia() * item.getSoluong()).sum();
        model.addAttribute("totalAmount", totalAmount);

        return "khachhang/ThanhToan";
    }




    @PostMapping("/thanhtoan")
    public String xuLyThanhToan(@RequestParam double totalAmount,
                                @RequestParam("selectedItems") String selectedItemsJson,
                                Model model, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Log dữ liệu nhận được
            System.out.println("Total Amount: " + totalAmount);
            System.out.println("Selected Items JSON: " + selectedItemsJson);

            String tenDangNhap = userDetails.getUsername();
            TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);
            if (taiKhoanDto == null || taiKhoanDto.getKhachHangDTO() == null) {
                return "redirect:/login";
            }

            // Chuyển đổi JSON thành danh sách Integer
            ObjectMapper objectMapper = new ObjectMapper();
            List<Integer> selectedItems = Arrays.asList(objectMapper.readValue(selectedItemsJson, Integer[].class));

            System.out.println("Selected Items: " + selectedItems); // Log danh sách sản phẩm đã chọn

            if (selectedItems.isEmpty()) {
                model.addAttribute("message", "Không có sản phẩm nào được chọn.");
                return "khachhang/GioHang";
            }

            int khachHangId = taiKhoanDto.getKhachHangDTO().getId();
            System.out.println("KhachHang ID: " + khachHangId); // Log ID khách hàng

            List<GioHang> cartItems = gioHangService.getCartItemsByIds(khachHangId, selectedItems);
            System.out.println("Cart Items: " + cartItems); // Log danh sách sản phẩm trong giỏ hàng

            if (cartItems == null || cartItems.isEmpty()) {
                model.addAttribute("message", "Không có sản phẩm nào được chọn.");
                return "khachhang/GioHang";
            }

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalAmount", totalAmount);
            return "khachhang/ThanhToan";

        } catch (IOException e) {
            model.addAttribute("message", "Có lỗi xảy ra khi xử lý dữ liệu giỏ hàng.");
            return "khachhang/GioHang";
        }
    }

    @PostMapping("/thanhtoan/hoadon")
    public String xuLyHoaDon(@RequestParam double totalAmount,
                             @RequestParam String paymentMethod,
                             @RequestParam String note,
                             @AuthenticationPrincipal UserDetails userDetails, Model model) {
        String tenDangNhap = userDetails.getUsername();
        TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);

        if (taiKhoanDto == null || taiKhoanDto.getKhachHangDTO() == null) {
            return "redirect:/login";
        }

        KhachHangDTO khachHangDto = taiKhoanDto.getKhachHangDTO();
        if (khachHangDto == null) {
            model.addAttribute("message", "Không tìm thấy thông tin khách hàng.");
            return "khachhang/ThongBaoLoi";
        }

        KhachHang khachHang = new KhachHang();
        khachHang.setId(khachHangDto.getId());
        khachHang.setMakhachhang(khachHangDto.getMakhachhang());
        khachHang.setTenkhachhang(khachHangDto.getTenkhachhang());
        khachHang.setSdt(khachHangDto.getSdt());
        khachHang.setDiachi(khachHangDto.getDiachi());

        int khachHangId = khachHang.getId();
        List<GioHang> cartItems = gioHangService.getCartItems(khachHangId);

        if (cartItems == null || cartItems.isEmpty()) {
            model.addAttribute("message", "Giỏ hàng của bạn đang trống.");
            return "khachhang/GioHang";
        }

        // Tạo hóa đơn
        HoaDon hoaDon = new HoaDon();
        String maHoaDonCT = "HD" + (int)(Math.random() * 100000);
        hoaDon.setMahoadon(maHoaDonCT);
        hoaDon.setKhachHang(khachHang);
        hoaDon.setTongtien(BigDecimal.valueOf(totalAmount));
        hoaDon.setNgaytao(LocalDate.now());
        hoaDon.setNgaymua(LocalDate.now());
        hoaDon.setTrangthai(1);
        hoaDon.setDiaChi(khachHang.getDiachi());
        hoaDon.setGhiChu(note);
        hoaDon.setActive(true);
        hoaDon.setChiTietHoaDons(new ArrayList<>());

        for (GioHang item : cartItems) {
            HoaDonCT chiTiet = new HoaDonCT();
            chiTiet.setMahoadonct( "HDCT" + (int)(Math.random() * 10000));
            chiTiet.setSanPhamChiTiet(item.getSanPhamChiTiet());
            chiTiet.setSoluong(item.getSoluong());
            chiTiet.setDongia(BigDecimal.valueOf(item.getSanPhamChiTiet().getDongia()));
            chiTiet.setNgaytao(LocalDate.now());
            chiTiet.setTongtien(chiTiet.getDongia().multiply(BigDecimal.valueOf(chiTiet.getSoluong())));
            chiTiet.setHinhthucthanhtoan(paymentMethod);
            chiTiet.setHoaDon(hoaDon);
            hoaDon.getChiTietHoaDons().add(chiTiet);
        }

        hoaDonService.save(hoaDon);

        // Tạo đơn hàng
        DonHang donHang = new DonHang();

        donHang.setHoaDon(hoaDon);
        donHang.setKhachHang(khachHang);
        donHang.setNgaytao(LocalDate.now());
        donHang.setTongtien(BigDecimal.valueOf(totalAmount));
        donHang.setDiaChi(khachHang.getDiachi());
        donHang.setSdt(khachHang.getSdt());
        donHang.setTrangThai("Chờ duyệt");
        donHang.setGhiChu(note);
        donHang.setLydohuy("");

        donHangRepository.save(donHang);

        // Xóa giỏ hàng sau khi thanh toán thành công
        gioHangService.clearCart(khachHangId);

        model.addAttribute("message", "Đơn hàng của bạn đã được đặt thành công!");
        return "khachhang/ThanhToanThanhCong"; // Tên trang thông báo thanh toán thành công
    }





}


