package com.example.demo.Controller.giohang;

import com.example.demo.Controller.login.UserUtils;
import com.example.demo.entity.*;
import com.example.demo.DTO.KhachHangDTO;
import com.example.demo.respository.SanPhamChiTietRepository;
import com.example.demo.respository.nhanVien.DonHangRepository;
import com.example.demo.service.*;
import com.example.demo.services.KhachHangService;
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
    private KhachHangService khachHangService;
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private DonHangRepository donHangRepository;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository ;

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
    @ResponseBody
    public Integer findSanPhamChiTietId(@RequestParam("sizeId") Integer sizeId,
                                        @RequestParam("colorId") Integer colorId,
                                        @RequestParam("productId") Integer productId) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findBySizeIdColorIdAndProductId(sizeId, colorId, productId);
        return sanPhamChiTiet != null ? sanPhamChiTiet.getId() : null;
    }



    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@RequestBody Map<String, Object> payload
            ,
                                                @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        try {
            int sanPhamChiTietId = (int) payload.get("sanPhamChiTietId");
            int quantity = (int) payload.get("quantity");

            System.out.println("Received updateQuantity request for: " + sanPhamChiTietId + " quantity: " + quantity);

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


//    @PostMapping("/thanhtoan")
//    public String xuLyThanhToan(@RequestParam("totalAmount") String totalAmountStr,
//                                @RequestParam("selectedItems") String selectedItemsJson,
//                                Model model, @AuthenticationPrincipal UserDetails userDetails) {
//        try {
//            // Chuyển đổi totalAmount từ chuỗi sang BigDecimal
//            BigDecimal totalAmount = new BigDecimal(totalAmountStr);
//
//            String tenDangNhap = userDetails.getUsername();
//            TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);
//            if (taiKhoanDto == null || taiKhoanDto.getKhachHangDTO() == null) {
//                return "redirect:/login";
//            }
//
//            // Chuyển đổi JSON thành danh sách Integer
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<Integer> selectedItems = Arrays.asList(objectMapper.readValue(selectedItemsJson, Integer[].class));
//
//            if (selectedItems.isEmpty()) {
//                model.addAttribute("message", "Không có sản phẩm nào được chọn.");
//                return "khachhang/GioHang";
//            }
//
//            int khachHangId = taiKhoanDto.getKhachHangDTO().getId();
//            List<GioHang> cartItems = gioHangService.getCartItemsByIds(khachHangId, selectedItems);
//
//            if (cartItems == null || cartItems.isEmpty()) {
//                model.addAttribute("message", "Không có sản phẩm nào được chọn.");
//                return "khachhang/GioHang";
//            }
//
//            // Chuyển đổi dữ liệu byte array thành chuỗi base64
//            List<Map<String, Object>> processedCartItems = new ArrayList<>();
//            for (GioHang item : cartItems) {
//                Map<String, Object> itemMap = new HashMap<>();
//                itemMap.put("id", item.getId());
//                itemMap.put("sanPhamChiTiet", item.getSanPhamChiTiet());
//                itemMap.put("soluong", item.getSoluong());
//
//                if (item.getSanPhamChiTiet().getAnhSanPhamChiTiet() != null) {
//                    String base64Image = Base64.getEncoder().encodeToString(item.getSanPhamChiTiet().getAnhSanPhamChiTiet());
//                    System.out.println("Base64 Image: " + base64Image); // Log dữ liệu base64 chi tiết
//                    itemMap.put("anhspBase64", base64Image);
//                } else {
//                    itemMap.put("anhspBase64", ""); // Cập nhật giá trị rỗng nếu không có ảnh
//                }
//
//                processedCartItems.add(itemMap);
//            }
//
//            model.addAttribute("cartItems", processedCartItems);
//            model.addAttribute("totalAmount", totalAmount);
//            return "khachhang/ThanhToan";
//
//        } catch (IOException | NumberFormatException e) {
//            model.addAttribute("message", "Có lỗi xảy ra khi xử lý dữ liệu giỏ hàng.");
//            return "khachhang/GioHang";
//        }
//    }

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
        hoaDon.setThanhtien(BigDecimal.valueOf(totalAmount));
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
        donHang.setTrangthaioffline(false);
        donHang.setLydohuy("");

        donHangRepository.save(donHang);

        // Xóa giỏ hàng sau khi thanh toán thành công
        gioHangService.clearCart(khachHangId);

        model.addAttribute("message", "Đơn hàng của bạn đã được đặt thành công!");
        return "khachhang/ThanhToanThanhCong"; // Tên trang thông báo thanh toán thành công
    }





    @PostMapping("/thanhtoan")
    public String xuLyThanhToan(@RequestParam("totalAmount") String totalAmountStr,
                                @RequestParam("selectedItems") String selectedItemsJson,
                                Model model, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Chuyển đổi totalAmount từ chuỗi sang BigDecimal
            BigDecimal totalAmount = new BigDecimal(totalAmountStr);

            String tenDangNhap = userDetails.getUsername();
            KhachHang khachHang = khachHangService.findByTenTaiKhoan(tenDangNhap);
            if (khachHang == null) {
                return "redirect:/login";
            }

            // Log dữ liệu KhachHang để kiểm tra
            System.out.println("KhachHang: " + khachHang);

            // Chuyển đổi JSON thành danh sách Integer
            ObjectMapper objectMapper = new ObjectMapper();
            List<Integer> selectedItems = Arrays.asList(objectMapper.readValue(selectedItemsJson, Integer[].class));

            if (selectedItems.isEmpty()) {
                model.addAttribute("message", "Không có sản phẩm nào được chọn.");
                return "khachhang/GioHang";
            }

            int khachHangId = khachHang.getId();
            List<GioHang> cartItems = gioHangService.getCartItemsByIds(khachHangId, selectedItems);

            if (cartItems == null || cartItems.isEmpty()) {
                model.addAttribute("message", "Không có sản phẩm nào được chọn.");
                return "khachhang/GioHang";
            }

            // Chuyển đổi dữ liệu byte array thành chuỗi base64
            List<Map<String, Object>> processedCartItems = new ArrayList<>();
            for (GioHang item : cartItems) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("id", item.getId());
                itemMap.put("sanPhamChiTiet", item.getSanPhamChiTiet());
                itemMap.put("soluong", item.getSoluong());

                if (item.getSanPhamChiTiet().getAnhSanPhamChiTiet() != null) {
                    String base64Image = Base64.getEncoder().encodeToString(item.getSanPhamChiTiet().getAnhSanPhamChiTiet());
                    itemMap.put("anhspBase64", base64Image);
                } else {
                    itemMap.put("anhspBase64", ""); // Giá trị rỗng nếu không có ảnh
                }

                processedCartItems.add(itemMap);
            }

            model.addAttribute("cartItems", processedCartItems);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("khachHang", khachHang); // Thêm thông tin khách hàng vào model
            return "khachhang/ThanhToan";

        } catch (IOException | NumberFormatException e) {
            model.addAttribute("message", "Có lỗi xảy ra khi xử lý dữ liệu giỏ hàng.");
            return "khachhang/GioHang";
        }
    }




}


