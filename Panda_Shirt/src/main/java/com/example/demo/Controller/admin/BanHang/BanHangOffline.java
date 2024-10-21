package com.example.demo.Controller.admin.BanHang;

import com.example.demo.entity.HoaDon;
import com.example.demo.respository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

@Controller
@RequestMapping("/panda/banhangoffline")
public class BanHangOffline {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @GetMapping()
    public String hienthi(Model model){
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "admin/BanHang/BanHangOffline";
    }
    // tạo mã hóa đơn tự động
    private static final HashMap<String,Integer> demstt = new HashMap<>();

    @PostMapping("/taohd")
    public ResponseEntity<HoaDon> taohoadon(){
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int stt = demstt.getOrDefault(date,0) + 1;
        demstt.put(date,stt);
        String mahd = String.format("HD%s%03d",date,stt);
        int soluong = 0 ;
        LocalDate ngaymua = LocalDate.now();
        LocalDate ngaytao = LocalDate.now();
        LocalDate ngaysua = null;
        Double dongia = 0.0;
        String sdt= null ;
        Double tong = 0.0;
        Double thanhtien = 0.0;
        int trangthai = 1 ;
        HoaDon response = new HoaDon(mahd,soluong,BigDecimal.valueOf(dongia),sdt,ngaymua,ngaytao,ngaysua,
                BigDecimal.valueOf(dongia),BigDecimal.valueOf(dongia),trangthai);
        // Tạo đối tượng phản hồi
        hoaDonRepository.save(response);
        return ResponseEntity.ok(response);
    }
}
