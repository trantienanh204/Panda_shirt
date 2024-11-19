package com.example.demo.Controller.admin.BanHang;

import com.example.demo.DTO.HoaDonCTDTO;
import com.example.demo.entity.*;
import com.example.demo.respository.*;
import com.example.demo.services.BanHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
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
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    NhanVienRespository nhanVienRespository;
    @Autowired
    KhachHangRepository khachHangRepository;


    private int idhd ;
    @GetMapping()
    public String hienthi(Model model){
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        List<HoaDon> hoaDon = hoaDonRepository.findHoaDonsWithNullId();
        List<Voucher> voucher = voucherRepository.findAll();

        List<Voucher> validVouchers = new ArrayList<>();
        for (Voucher v : voucher) {
            try {
                int soLuong = Integer.parseInt(v.getSoLuong());
                if (soLuong > 0 ) {
                    validVouchers.add(v);
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi : " + v.getMa());
            }
        }

        model.addAttribute("hoaDons", hoaDon);
        model.addAttribute("lsvoucher",validVouchers);
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
            newhd.setNgaytao(LocalDate.now());
            hoaDonRepository.save(newhd);
            Map<String, String> response = new HashMap<>();
            response.put("mahoadon", mahd);
            BigDecimal tongTien = hoaDonCTRepository.TongTienByHoaDonId(idhd);
            model.addAttribute("tongTien", tongTien);
            return ResponseEntity.ok(response);
        }

     BigDecimal tongtien(int id){
            this.idhd = id ;
            return hoaDonCTRepository.TongTienByHoaDonId(idhd);
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
//        BigDecimal tongTien = hoaDonCTRepository.TongTienByHoaDonId(idhd);
        BigDecimal tt = tongtien(idhd);
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        String tongTien = decimalFormat.format(tt);
        String thanhtien = decimalFormat.format(tt);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("tongtien", tt);
        model.addAttribute("thanhtien", thanhtien);
        model.addAttribute("thanhTien", tt);
        model.addAttribute("hoaDonCTList", hoaDonCTList);
        model.addAttribute("idhoadon", idhoadon);
        return "forward:/panda/banhangoffline";
    }
//
//    @PostMapping("/taohdct")
//    public ResponseEntity<String> createHoaDonCT(@RequestBody HoaDonCTDTO dto,Model model) {
//        HoaDonCT hoaDonCT = new HoaDonCT();
//        System.out.println("Received DTO: " + hoaDonCT);
//        System.out.println("idhd = " + idhd);
//        try {
//            hoaDonCT.setHoaDon(new HoaDon(idhd));
//            hoaDonCT.setSanPhamChiTiet(new SanPhamChiTiet(dto.getIdSanPhamCT()));
//            hoaDonCT.setDongia(dto.getDonGia());
//            hoaDonCT.setSoluong(dto.getSoLuong());
//
//            BigDecimal donGia = dto.getDonGia();
//            int soLuong = dto.getSoLuong();
//            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));
//
//            hoaDonCT.setTongtien(thanhTien);
//            hoaDonCT.setNgaytao(LocalDate.now());
//            BigDecimal tongTien = hoaDonCTRepository.TongTienByHoaDonId(idhd);
//            model.addAttribute("tongTien", tongTien);
//            hoaDonCTRepository.save(hoaDonCT);
//            return ResponseEntity.ok("Hóa đơn chi tiết đã được tạo thành công!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo hóa đơn chi tiết!");
//        }
//    }
    @PostMapping("/taohdct")
    public ResponseEntity<String> createHoaDonCT(@RequestBody HoaDonCTDTO dto, Model model) {
        HoaDonCT hoaDonCT = new HoaDonCT();
            hoaDonCT.setHoaDon(new HoaDon(idhd));
            hoaDonCT.setSanPhamChiTiet(new SanPhamChiTiet(dto.getIdSanPhamCT()));
            hoaDonCT.setDongia(dto.getDonGia());
            hoaDonCT.setSoluong(dto.getSoLuong());

            HoaDonCT existingHoaDonCT = hoaDonCTRepository.findByHoaDonIdAndSanPhamId(idhd, dto.getIdSanPhamCT());
            if (existingHoaDonCT != null) {

                existingHoaDonCT.setSoluong(existingHoaDonCT.getSoluong() + 1);
                existingHoaDonCT.setId(existingHoaDonCT.getId());
                BigDecimal totalPrice = existingHoaDonCT.getDongia().multiply(BigDecimal.valueOf(existingHoaDonCT.getSoluong()));
                existingHoaDonCT.setTongtien(totalPrice);
                System.out.println("id hdct : " + existingHoaDonCT.getId());
                hoaDonCTRepository.save(existingHoaDonCT);
                return ResponseEntity.ok("Thành công !");
            }
            // Nếu không tồn tại, tạo mới
            BigDecimal donGia = dto.getDonGia();
            int soLuong = dto.getSoLuong();
            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));
            hoaDonCT.setTongtien(thanhTien);
            hoaDonCT.setNgaytao(LocalDate.now());
            System.out.println("add");
            hoaDonCTRepository.save(hoaDonCT);
            return ResponseEntity.ok("Thành công !");
    }
    @PostMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody HoaDonCTDTO dto,Model model) {
            HoaDonCT hoaDonCT = hoaDonCTRepository.findById(dto.getIdHoadon())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn chi tiết"));
            System.out.println("idspct = "  +dto.getIdSanPhamCT());
            Optional<SanPhamChiTiet> spctOptional = sanPhamChiTietRepository.findById(dto.getIdSanPhamCT());
            if (spctOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm chi tiết.");
            }
            SanPhamChiTiet spct = spctOptional.get();

            // Kiểm tra số lượng tồn
            if (dto.getSoLuong() > spct.getSoluongsanpham()) {
                return ResponseEntity.badRequest().body("Số lượng nhập vượt quá số lượng tồn kho.");
            }


            hoaDonCT.setSoluong(dto.getSoLuong());
            hoaDonCT.setTongtien(dto.getThanhTien());
            hoaDonCT.setTrangthai(1);
            BigDecimal tongTien = tongtien(idhd);
            model.addAttribute("tongTien", tongTien);
            hoaDonCTRepository.save(hoaDonCT);
            return ResponseEntity.ok("Cập nhật thành công");
    }

    @GetMapping("/selectvc")
    public ResponseEntity<Map<String, String>> chonVoucher(@RequestParam("id") int id,Model model) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));
        System.out.println("Voucher ID nhận từ client: " + id);

        Map<String, String> response = new HashMap<>();
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        BigDecimal tt = tongtien(idhd);
        BigDecimal thanhtien ;
        String loai = voucher.getMucGiam();
        int mucgiam = Integer.parseInt(voucher.getMucGiam());
        if(voucher.isLoai()){
            BigDecimal mucGiamBD = new BigDecimal(mucgiam);
            BigDecimal sophantram = tt.divide(new BigDecimal(100), RoundingMode.HALF_UP).multiply(mucGiamBD);
            thanhtien = tt.subtract(new BigDecimal(String.valueOf(sophantram)));
            loai = voucher.getMucGiam() + " % ";
        }else{
            thanhtien = tt.subtract(new BigDecimal(mucgiam));
            BigDecimal giamgia = new BigDecimal(voucher.getMucGiam());
            String formattedgiamgia = decimalFormat.format(giamgia);
            loai = formattedgiamgia + " VND ";
        }
        String formattedThanhtien = decimalFormat.format(thanhtien);

        response.put("thanhtien", formattedThanhtien);
        response.put("thanhTien", String.valueOf(thanhtien));
        response.put("mavocher", voucher.getMa());;
        response.put("mucgiam", loai);
        response.put("idvoucher", String.valueOf(voucher.getId()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteHoaDonCT(@RequestParam Integer idHoaDonCT) {
        System.out.println("ID hd : " + idHoaDonCT);
        try {
            HoaDonCT hoaDonCT = hoaDonCTRepository.findById(idHoaDonCT)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Hóa đơn chi tiết"));

            hoaDonCTRepository.delete(hoaDonCT);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi xóa");
        }
    }

    @PostMapping("/thanhtoan")
    public String thanhtoan(
            @RequestParam("idhoadon") int idhoadon,
            @RequestParam(value = "idvoucher", required = false) Integer idvoucher,
            @RequestParam("thanhtien") BigDecimal thanhtien,
            @RequestParam("tongtien") BigDecimal tongtien,
            Model model
    ){
        System.out.println("thành công");
            HoaDon hd = hoaDonRepository.findById(idhoadon).orElse(null);
            if(hd == null){
                model.addAttribute("loi","Hóa đơn");
            }
            NhanVien nv = nhanVienRespository.findById(1).orElse(null);
            if(nv == null){
                model.addAttribute("loi","Nhân viên");
            }
            KhachHang kh = khachHangRepository.findById(1).orElse(null);
            if(kh == null){
                model.addAttribute("loi","Khách hàng");
            }
            Voucher vc = null;
            if (idvoucher != null && idvoucher > 0) {
            vc = voucherRepository.findById(idvoucher).orElse(null);


            if (vc != null && Integer.parseInt(vc.getSoLuong()) > 0) {
                // Giảm số lượng voucher đi 1
                int currentQuantity = Integer.parseInt(vc.getSoLuong());
                currentQuantity -= 1;
                vc.setSoLuong(String.valueOf(currentQuantity));
                voucherRepository.save(vc);
            } else {
                // Xử lý khi voucher không còn hoặc không hợp lệ
                // Có thể ném một exception hoặc trả về thông báo lỗi
                throw new RuntimeException("Voucher không hợp lệ hoặc đã hết số lượng");
            }
        }
            List<HoaDonCT> lshdct = hoaDonCTRepository.findhdct(idhoadon);
            for (HoaDonCT hdct : lshdct) {
                SanPhamChiTiet spct = hdct.getSanPhamChiTiet();
                if (spct != null) {
                    int soLuongTon = spct.getSoluongsanpham();
                    int soLuongBan = hdct.getSoluong();
                    spct.setSoluongsanpham(soLuongTon - soLuongBan);
                    sanPhamChiTietRepository.save(spct);
                }
            }

            hd.setNhanVien(nv);
            hd.setKhachHang(kh);
            hd.setVoucher(vc);
            hd.setThanhtien(thanhtien);
            hd.setTongtien(tongtien);
            hd.setTrangthai(1);
            hd.setNgaymua(LocalDate.now());
            hoaDonRepository.save(hd);
        return "redirect:/panda/banhangoffline";

    }
}
