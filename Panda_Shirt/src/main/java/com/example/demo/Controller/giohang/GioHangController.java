package com.example.demo.Controller.giohang;

import com.example.demo.entity.GioHang;
import com.example.demo.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/panda/giohang")
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam int khachHangId,
                                            @RequestParam int sanPhamChiTietId,
                                            @RequestParam int quantity) {
        try {
            gioHangService.addToCart(khachHangId, sanPhamChiTietId, quantity);
            return ResponseEntity.ok("Sản phẩm đã được thêm vào giỏ hàng.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }
//    @GetMapping("/getallgiohang")
//    public GioHang fillgiohang(@RequestParam int khachHangId) {
//            gioHangService.getallgiohang(khachHangId);
//            return gioHangService.getallgiohang(khachHangId);
//        }
    }


