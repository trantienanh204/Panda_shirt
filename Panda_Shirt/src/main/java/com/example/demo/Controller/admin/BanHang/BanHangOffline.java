package com.example.demo.Controller.admin.BanHang;

import com.example.demo.DTO.HoaDonCTDTO;
import com.example.demo.DTO.ThanhTienDTO;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonCT;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.respository.HoaDonCTRepository;
import com.example.demo.respository.HoaDonRepository;
import com.example.demo.respository.SanPhamChiTietRepository;
import com.example.demo.services.BanHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/panda/banhangoffline")
public class BanHangOffline {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    BanHangService banHangService;
    @Autowired
    HoaDonCTRepository hoaDonCTRepository;

    private int idhd ;
    @GetMapping()
    public String hienthi(Model model){
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        List<HoaDon> hoaDon = hoaDonRepository.findHoaDonsWithNullId();
        model.addAttribute("hoaDons", hoaDon);

        return "admin/BanHang/BanHangOffline";
    }
    @GetMapping("/suggestions")
    @ResponseBody
    public List<String> getSuggestions() {
        return sanPhamChiTietRepository.findAll().stream()
                .map(sp -> String.format("%s - %s - %s - %s",
                        sp.getSanPham().getMasp(),
                        sp.getSanPham().getTensp(),
                        sp.getMauSac().getTen(),
                        sp.getKichThuoc().getTen()))
                .collect(Collectors.toList());

    }
    @GetMapping("/find")
    @ResponseBody
    public List<String> getfindten(@RequestParam("keyword") String keyword) {
        System.out.println("keyword: " + keyword);
        List<String> suggestions = new ArrayList<>();
        try {
            suggestions = banHangService.findByTenSanPham(keyword).stream()
                    .filter(sp -> sp.getSanPham() != null && sp.getMauSac() != null && sp.getKichThuoc() != null)
                    .map(sp -> String.format("%s - %s - %s - %s - %s",
                            sp.getId(),
                            sp.getSanPham().getTensp(),
                            sp.getMauSac().getTen(),
                            sp.getKichThuoc().getTen(),
                            sp.getDongia()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suggestions;
    }

    @GetMapping("/hoadon")
    public ResponseEntity<List<HoaDon>> getEmptyHoaDons() {
        List<HoaDon> hoaDons = hoaDonRepository.findHoaDonsWithNullId();
        return ResponseEntity.ok(hoaDons);
    }
    // tạo mã hóa đơn tự động
    private static final HashMap<String,Integer> demstt = new HashMap<>();
        @PostMapping("/taohd")
        public ResponseEntity<Map<String, String>> taohoadon(Model model) {
            String hd = hoaDonRepository.findMaxMaHoaDon();
            int demhd;
            if (hd == null) {
                // Nếu chưa có hóa đơn nào thì bắt đầu từ 1
                demhd = 1;
            } else {
                // Cắt chuỗi để lấy phần số sau 'HD' và chuyển đổi sang số nguyên
                demhd = Integer.parseInt(hd.substring(2)) + 1;
            }
            String mahd = String.format("HD%03d", demhd);

            // Tạo mã hóa đơn
            HoaDon newhd = new HoaDon();
            newhd.setMahoadon(mahd);
            newhd.setActive(true);
            hoaDonRepository.save(newhd);
            Map<String, String> response = new HashMap<>();
            response.put("mahoadon", mahd);
            BigDecimal tongTien = hoaDonCTRepository.TongTienByHoaDonId(idhd);
            model.addAttribute("tongTien", tongTien);
            return ResponseEntity.ok(response);
        }

    @GetMapping("/muahang/{id}")
    public String muahang(@PathVariable("id") int idhoadon,Model model){
        this.idhd = idhoadon;
        List<HoaDon> hoaDons = hoaDonRepository.findAll();
        // Đánh dấu hóa đơn được chọn là active
        for (HoaDon hoaDon : hoaDons) {
            if (hoaDon.getId() == idhoadon) {
                hoaDon.setActive(true);
            } else {
                hoaDon.setActive(false);
            }
        }

        List<HoaDonCT> hoaDonCTList = hoaDonCTRepository.findhoadonct(idhoadon);
        BigDecimal tongTien = hoaDonCTRepository.TongTienByHoaDonId(idhd);
        model.addAttribute("hoaDonCTList", hoaDonCTList);
        model.addAttribute("idhoadon", idhoadon);
        model.addAttribute("tongTien", tongTien);
        return "forward:/panda/banhangoffline";
    }

    @PostMapping("/taohdct")
    public ResponseEntity<String> createHoaDonCT(@RequestBody HoaDonCTDTO dto,Model model) {
        // Xử lý tạo hóa đơn chi tiết trong cơ sở dữ liệu
        HoaDonCT hoaDonCT = new HoaDonCT();
        System.out.println("Received DTO: " + hoaDonCT);
        System.out.println("idhd = " + idhd);
        try {
            hoaDonCT.setHoaDon(new HoaDon(idhd));
            hoaDonCT.setSanPhamChiTiet(new SanPhamChiTiet(dto.getIdSanPhamCT()));
            hoaDonCT.setDongia(dto.getDonGia());
            hoaDonCT.setSoluong(dto.getSoLuong());

            BigDecimal donGia = dto.getDonGia();
            int soLuong = dto.getSoLuong();
            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));

            hoaDonCT.setTongtien(thanhTien);
            hoaDonCT.setNgaytao(LocalDate.now());
            BigDecimal tongTien = hoaDonCTRepository.TongTienByHoaDonId(idhd);
            model.addAttribute("tongTien", tongTien);
            hoaDonCTRepository.save(hoaDonCT);
            return ResponseEntity.ok("Hóa đơn chi tiết đã được tạo thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo hóa đơn chi tiết!");
        }
    }

//    @PostMapping("/update")
//    public ResponseEntity<String> updateProduct(@RequestBody HoaDonCTDTO dto,Model model) {
//        // Lấy `HoaDonCT` từ `idSanPhamCT` và cập nhật số lượng, đơn giá, thành tiền
//            // Lấy đối tượng HoaDonCT từ cơ sở dữ liệu dựa trên ID
//            HoaDonCT hoaDonCT = hoaDonCTRepository.findById(dto.getIdHoadon())
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn chi tiết"));
//            // Cập nhật số lượng và tổng tiền
//            hoaDonCT.setSoluong(dto.getSoLuong());
//            hoaDonCT.setTongtien(dto.getThanhTien());
//
//            BigDecimal tongTien = hoaDonCTRepository.TongTienByHoaDonId(idhd);
//            model.addAttribute("tongTien", tongTien);
//            // Lưu đối tượng HoaDonCT đã cập nhật lại vào cơ sở dữ liệu
//            hoaDonCTRepository.save(hoaDonCT);
//            return ResponseEntity.ok("Cập nhật thành công");
//    }

    @PostMapping("/update")
    @ResponseBody
    public ThanhTienDTO updateHoaDon(
                                     @RequestParam("soLuong") int soluong,
                                     @RequestParam("thanhTien") BigDecimal tongTien) {

        // Tìm HoaDonCT dựa trên id
        HoaDonCT hoaDonCT = hoaDonCTRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn chi tiết"));

        // Cập nhật số lượng và tính lại tổng tiền cho HDCT
        hoaDonCT.setSoluong(soluong);
        hoaDonCT.setTongtien(tongTien);

        // Lưu lại vào cơ sở dữ liệu
        hoaDonCTRepository.save(hoaDonCT);

        // Tính lại tổng thành tiền cho toàn bộ hóa đơn
        BigDecimal thanhTien = hoaDonCTRepository.TongTienByHoaDonId(hoaDonCT.getHoaDon().getId());

        // Trả về tổng thành tiền mới trong DTO
        return new ThanhTienDTO(thanhTien);
    }

}
