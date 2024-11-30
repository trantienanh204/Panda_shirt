package com.example.demo.Controller.thanhtoanonline;

import com.example.demo.DTO.KhachHangDTO;
import com.example.demo.DTO.TaiKhoanDTO;
import com.example.demo.entity.*;
import com.example.demo.respository.HoaDonCTRepository;
import com.example.demo.respository.HoaDonRepository;
import com.example.demo.respository.SanPhamChiTietRepository;
import com.example.demo.respository.nhanVien.DonHangRepository;
import com.example.demo.service.GioHangService;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.TaiKhoanService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api")
public class VnpayController {

    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private DonHangRepository donHangRepository;
    @Autowired
    private HoaDonCTRepository hoaDonCTRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("totalAmount") double totalAmount,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam("paymentMethod") String paymentMethod,
                              HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if ("BankTransfer".equals(paymentMethod)) {
            String vnp_ReturnUrl = "http://localhost:8080/api/vnpay-payment";
            String vnpayUrl = vnPayService.createOrder((int) totalAmount, orderInfo);

            request.getSession().setAttribute("orderInfo", orderInfo);
            request.getSession().setAttribute("totalAmount", totalAmount);
            request.getSession().setAttribute("paymentMethod", paymentMethod);

            return "redirect:" + vnpayUrl;
        } else {
            return "redirect:/api/processInvoice?totalAmount=" + totalAmount + "&paymentMethod=" + paymentMethod + "&note=" + UriUtils.encodePath(orderInfo, StandardCharsets.UTF_8);
        }
    }

    @GetMapping("/processInvoice")
    public String processInvoice(@RequestParam double totalAmount,
                                 @RequestParam String paymentMethod,
                                 @RequestParam String note,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        String username = userDetails.getUsername();
        TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(username);

        if (taiKhoanDto == null || taiKhoanDto.getKhachHangDTO() == null) {
            return "/panda/login";
        }

        KhachHang khachHang = mapToKhachHang(taiKhoanDto.getKhachHangDTO());
        int khachHangId = khachHang.getId();
        List<GioHang> cartItems = gioHangService.getCartItems(khachHangId);

        if (cartItems == null || cartItems.isEmpty()) {
            model.addAttribute("message", "Giỏ hàng của bạn đang trống.");
            return "khachhang/GioHang";
        }

        // Tạo hóa đơn và đơn hàng
        HoaDon hoaDon = createHoaDon(khachHang, cartItems, totalAmount, note, paymentMethod);
        DonHang donHang = createDonHang(khachHang, hoaDon, totalAmount, note, paymentMethod);

        // Lưu dữ liệu vào cơ sở dữ liệu
        hoaDonService.save(hoaDon);
        donHangRepository.save(donHang);

        // Xóa giỏ hàng
        gioHangService.clearCart(khachHangId);

        // Thêm thông báo vào RedirectAttributes
        redirectAttributes.addFlashAttribute("successMessage", "Đơn hàng của bạn đã được đặt thành công!");

        // Chuyển hướng tới trang chủ với thông báo thành công
        return "redirect:/panda/trangchu";
    }

    @GetMapping("/vnpay-payment")
    public String handleVnPayReturn(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        int paymentStatus = vnPayService.orderReturn(request);

        if (paymentStatus != 1) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thanh toán không thành công. Vui lòng thử lại.");
            return "redirect:/panda/thatbai";
        }

        // Lấy thông tin đơn hàng từ session (hoặc database)
        String orderInfo = (String) request.getSession().getAttribute("orderInfo");
        Double totalAmount = (Double) request.getSession().getAttribute("totalAmount"); // Chuyển đổi sang Double
        String paymentMethod = (String) request.getSession().getAttribute("paymentMethod"); // Lấy phương thức thanh toán từ session

        // Kiểm tra các giá trị
        if (orderInfo == null || totalAmount == null || paymentMethod == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại.");
            return "redirect:/panda/thatbai";
        }

        // Thực hiện xử lý hóa đơn
        return processInvoice(totalAmount, paymentMethod, orderInfo, userDetails, model, redirectAttributes);
    }


    private KhachHang mapToKhachHang(KhachHangDTO dto) {
        KhachHang khachHang = new KhachHang();
        khachHang.setId(dto.getId());
        khachHang.setMakhachhang(dto.getMakhachhang());
        khachHang.setTenkhachhang(dto.getTenkhachhang());
        khachHang.setSdt(dto.getSdt());
        khachHang.setDiachi(dto.getDiachi());
        return khachHang;
    }

    private HoaDon createHoaDon(KhachHang khachHang, List<GioHang> cartItems, double totalAmount, String note, String paymentMethod) {
        HoaDon hoaDon = new HoaDon();
        String hd = hoaDonRepository.findMaxMaHoaDon();
        int demhd;
        if (hd == null) {
            demhd = 1; // Nếu chưa có hóa đơn nào thì bắt đầu từ 1
        } else {
            try {
                demhd = Integer.parseInt(hd.substring(2)) + 1;
            } catch (NumberFormatException e) {
                throw new RuntimeException("Lỗi định dạng số từ mã hóa đơn: " + hd, e);
            }
        }
        String mahd = String.format("HD%03d", demhd);

        hoaDon.setMahoadon(mahd);
        hoaDon.setKhachHang(khachHang);
        hoaDon.setTongtien(BigDecimal.valueOf(totalAmount));
        hoaDon.setThanhtien(BigDecimal.valueOf(totalAmount));
        hoaDon.setNgaytao(LocalDate.now());
        hoaDon.setNgaymua(LocalDate.now());
        hoaDon.setTrangthai(1);
        hoaDon.setDiaChi(khachHang.getDiachi());
        hoaDon.setGhiChu(note);
        hoaDon.setActive(true);

        List<HoaDonCT> chiTietList = new ArrayList<>();
        for (GioHang item : cartItems) {
            HoaDonCT chiTiet = new HoaDonCT();
            String hdct = hoaDonCTRepository.findMaxhoadonct();
            int demhdct;
            if (hdct == null) {
                demhdct = 1; // Nếu chưa có hóa đơn chi tiết nào thì bắt đầu từ 1
            } else {
                try {
                    demhdct = Integer.parseInt(hdct.substring(4)) + 1; // Phải cắt chuỗi đúng cách
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Lỗi định dạng số từ mã hóa đơn chi tiết: " + hdct, e);
                }
            }
            String mahdct = String.format("HDCT%03d", demhdct);
            chiTiet.setMahoadonct(mahdct);
            chiTiet.setSanPhamChiTiet(item.getSanPhamChiTiet());
            chiTiet.setSoluong(item.getSoluong());
            chiTiet.setDongia(BigDecimal.valueOf(item.getSanPhamChiTiet().getDongia()));
            chiTiet.setNgaytao(LocalDate.now());
            chiTiet.setTongtien(chiTiet.getDongia().multiply(BigDecimal.valueOf(chiTiet.getSoluong())));
            chiTiet.setHinhthucthanhtoan(paymentMethod);
            chiTiet.setHoaDon(hoaDon);
            chiTietList.add(chiTiet);

            // Giảm số lượng sản phẩm trong kho
            SanPhamChiTiet sanPhamChiTiet = item.getSanPhamChiTiet();
            int soLuongConLai = sanPhamChiTiet.getSoluongsanpham() - item.getSoluong();
            if (soLuongConLai < 0) {
                throw new RuntimeException("Số lượng sản phẩm không đủ");
            }
            sanPhamChiTiet.setSoluongsanpham(soLuongConLai);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
        }
        hoaDon.setChiTietHoaDons(chiTietList);
        return hoaDon;
    }

    private DonHang createDonHang(KhachHang khachHang, HoaDon hoaDon, double totalAmount, String note, String paymentMethod) {
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

        // Đặt giá trị trangthaioffline dựa trên phương thức thanh toán
        if ("BankTransfer".equals(paymentMethod)) {
            donHang.setTrangthaioffline(true);
        } else {
            donHang.setTrangthaioffline(false);
        }

        return donHang;
    }
}
