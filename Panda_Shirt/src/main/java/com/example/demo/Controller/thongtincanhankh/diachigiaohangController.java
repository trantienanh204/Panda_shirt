package com.example.demo.Controller.thongtincanhankh;

import com.example.demo.DTO.KhachHangDTO;
import com.example.demo.DTO.TaiKhoanDTO;
import com.example.demo.entity.DiaChiDTO;
import com.example.demo.entity.KhachHang;
import com.example.demo.respository.KhachHangRepository;
import com.example.demo.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("diachi")
public class diachigiaohangController {

    @Autowired
    TaiKhoanService taiKhoanService;
    @Autowired
    KhachHangRepository khachHangRepository;

    @GetMapping("themdiachi")
    public String themdiachi(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String username = userDetails.getUsername();
        TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(username);
        KhachHang khachHang = mapToKhachHang(taiKhoanDto.getKhachHangDTO());
        model.addAttribute("khachhang", khachHang);
        return "khachhang/themdiachimoi";
    }

    @PostMapping("/api/save-address")
    public ResponseEntity<Map<String, String>> saveAddress(@RequestBody DiaChiDTO diaChiDTO, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(username);

        // Tìm khách hàng hiện có trong cơ sở dữ liệu
        KhachHang khachHang = khachHangRepository.findById(taiKhoanDto.getKhachHangDTO().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        // Cập nhật trường địa chỉ mà không làm mất thông tin cũ
        khachHang.setDiachi(diaChiDTO.getFullAddress());

        // Lưu thông tin khách hàng vào database
        khachHangRepository.save(khachHang);

        // Trả về đối tượng JSON hợp lệ
        Map<String, String> response = new HashMap<>();
        response.put("message", "Địa chỉ đã được lưu thành công");
        return ResponseEntity.ok(response);
    }

    private KhachHang mapToKhachHang(KhachHangDTO dto) {
        KhachHang khachHang = new KhachHang();
        khachHang.setId(dto.getId());
        khachHang.setTinhtrang(dto.getTinhtrang());
        khachHang.setDelete(dto.getDelete());
        khachHang.setMakhachhang(dto.getMakhachhang());
        khachHang.setTenkhachhang(dto.getTenkhachhang());
        khachHang.setSdt(dto.getSdt());
        khachHang.setDiachi(dto.getDiachi());
        return khachHang;
    }
}
