
package com.example.demo.Controller.sanpham;

import com.example.demo.entity.*;
import com.example.demo.respository.DanhMucRepository;
import com.example.demo.service.QuenmatkhauService;
import com.example.demo.service.hinhanhService;
import com.example.demo.service.sanPhamService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;

@RestController
@RequestMapping("panda")
public class sanphanController {

    private static final Logger logger = LoggerFactory.getLogger(sanphanController.class);
    @Autowired
    private hinhanhService hinhanhService;
    @Autowired
    private sanPhamService sanPhamService;
    @Autowired
    private QuenmatkhauService quenmatkhauService;
    @Autowired
    private DanhMucRepository danhMucRepository;
    @GetMapping("/sanpham/list")
    public Page<SanPham> getSanPhamList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "tensp", required = false) String tensp,
            @RequestParam(value = "trangthai", required = false) Integer trangthai) {


        return sanPhamService.hienThiSanPhamTheoDieuKien(page, tensp, trangthai);
    }


    @GetMapping("/Listsanpham")
    public List<Map<String, Object>> Listsanpham() {
        return sanPhamService.getallsp().stream().map(sp -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sp.getId());
            map.put("text", sp.getTensp());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/Listnxs")
    public List<Map<String, Object>> ListNXS() {
        return sanPhamService.getallNXS().stream().map(nxs -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", nxs.getId());
            map.put("text", nxs.getTennsx());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/Listdanhmuc")
    public List<Map<String, Object>> ListDanhmuc() {
        return sanPhamService.getallDanhmuc().stream().map(danhmuc -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", danhmuc.getId());
            map.put("text", danhmuc.getTendanhmuc());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/Listchatlieu")
    public List<Map<String, Object>> ListChatLieu() {
        return sanPhamService.getallCL().stream().map(chatlieu -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", chatlieu.getId());
            map.put("text", chatlieu.getTenChatLieu());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/Listthuonghieu")
    public List<Map<String, Object>> ListThuongHieu() {
        return sanPhamService.getallthuonghieu().stream().map(thuonghieu -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", thuonghieu.getId());
            map.put("text", thuonghieu.getTenthuonghieu());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/Listcoao")
    public List<Map<String, Object>> ListCoAo() {
        return sanPhamService.getallcoao().stream().map(coao -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", coao.getId());
            map.put("text", coao.getTen());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/Listkichthuoc")
    public List<Map<String, Object>> Listkichthuoc() {
        return sanPhamService.getallkichco().stream().map(coao -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", coao.getId());
            map.put("text", coao.getTen());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/Listmausac")
    public List<Map<String, Object>> Listmausac() {
        return sanPhamService.getallmausac().stream().map(mausac -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", mausac.getId());
            map.put("text", mausac.getTen());
            map.put("colorCode", mausac.getMa());
            return map;
        }).collect(Collectors.toList());
    }


//    @PostMapping("/themTatCa")
//    public ResponseEntity<?> themTatCa(@RequestBody ProductDTO productDTO) {
//
//        Product newProduct = productService.addProduct(productDTO);
//
//        return ResponseEntity.ok(newProduct);
//    }
@PostMapping("/taoSanPhamChiTiet")
public ResponseEntity<?> taoSanPhamChiTiet(@RequestBody sanPhamDTO productDTO) {
    if (productDTO == null) {
        return ResponseEntity.badRequest().body("Product data is required.");
    }

    try {
        // Chuyển đổi DTO sang entity SanPham
        SanPham newProduct = sanPhamService.convertToEntity(productDTO);

        // Tạo sản phẩm chi tiết tạm thời
        sanPhamService.createTemporarySanPhamChiTiet(newProduct, productDTO.getSizes(), productDTO.getColors());

        // Trả về danh sách chi tiết sản phẩm tạm thời
        return ResponseEntity.ok(sanPhamService.getTemporarySanPhamChiTietList());
    } catch (EntityNotFoundException e) {
        // Xử lý ngoại lệ nếu không tìm thấy dữ liệu
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + e.getMessage());
    } catch (IllegalArgumentException e) {
        // Xử lý ngoại lệ đối với dữ liệu không hợp lệ
        return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
    } catch (Exception e) {
        // Xử lý ngoại lệ chung
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product details: " + e.getMessage());
    }
}

    @PostMapping("/themTatCaSanPhamChiTiet")
    public ResponseEntity<?> themTatCaSanPhamChiTiet() {
        try {
            int savedCount = sanPhamService.saveTemporarySanPhamChiTiet();
            return ResponseEntity.ok(savedCount + " product details saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving product details: " + e.getMessage());
        }
    }


    @GetMapping("/danhSachSanPhamChiTiet")
    public ResponseEntity<?> getDanhSachSanPhamChiTiet() {
        try {
            // Thay đổi kiểu trả về cho nhất quán
            List<SanPhamChiTiet> productDetails = sanPhamService.getTemporarySanPhamChiTietList();
            return ResponseEntity.ok(productDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving product details: " + e.getMessage());
        }
    }

    @PutMapping("/capNhatSanPhamChiTiet/{id}")
    public ResponseEntity<?> capNhatSanPhamChiTiet(@PathVariable Long id, @RequestBody SanPhamChiTietDTO updatedProductDetail) {
        if (updatedProductDetail == null) {
            return ResponseEntity.badRequest().body("Product detail data is required");
        }

        try {
            // Gọi service để cập nhật thông tin sản phẩm chi tiết
            boolean updated = sanPhamService.updateTemporarySanPhamChiTiet(id, updatedProductDetail);
            if (updated) {
                return ResponseEntity.ok("Product detail updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product detail not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product detail: " + e.getMessage());
        }
    }


    @DeleteMapping("/xoaSanPhamChiTiet/{id}")
    public ResponseEntity<?> xoaSanPhamChiTiet(@PathVariable Long id) {
        try {
            boolean deleted = sanPhamService.deleteTemporarySanPhamChiTiet(id);
            if (deleted) {
                return ResponseEntity.ok("Product detail deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product detail not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product detail: " + e.getMessage());
        }
    }



    @PostMapping()
        public Object uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
            try {
                for (MultipartFile file : files) {
                    hinhanhService.luuHinhAnh(file);
                }
                return new RedirectView("baove/form");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
            }
        }

        @PostMapping("/tai-anh")
        public String taiAnh(@RequestParam("id") Integer id, Model model) {
            Optional<Anh_SP> hinhAnh = hinhanhService.findById(id);
            if (hinhAnh.isPresent()) {
                model.addAttribute("hinhAnh", hinhAnh.get());
            } else {
                model.addAttribute("hinhAnh", null);
            }
            return "index";
        }

        @GetMapping("xoa/{id}")
        public String xoa(@PathVariable Integer id) {
            if(id != null ){
                hinhanhService.xoa(id);
            }
            return "redirect:/baove/form";
        }


    @PostMapping("/addSP")
    public ResponseEntity<SanPham> addProduct(@RequestBody @Valid laytamDTO laytamDTO) {
        if (laytamDTO == null) {
            return ResponseEntity.badRequest().body(null);
        }

        SanPham sanPham = new SanPham();
        sanPham.setTensp(laytamDTO.getName());
        sanPham.setTrangthai(1);
        sanPham.setNgaytao(LocalDate.now());
        sanPham.setMasp(quenmatkhauService.random());

        try {
            sanPhamService.addsanpham(sanPham);
            logger.info("Successfully added product: {}", sanPham);
            return ResponseEntity.status(HttpStatus.CREATED).body(sanPham);
        } catch (Exception e) {
            logger.error("Error adding product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/addCoAo")
    public ResponseEntity<CoAo> addCollar(@RequestBody laytamDTO laytamDTO) {
        if (laytamDTO == null) {
            return ResponseEntity.badRequest().body(null);
        }

        CoAo coAo = new CoAo();
        coAo.setTen(laytamDTO.getName());
        coAo.setTrangThai(true);
        coAo.setNgayTao(LocalDateTime.now());
        coAo.setMa(quenmatkhauService.random());
        try {
            sanPhamService.addcoao(coAo);
            return ResponseEntity.status(HttpStatus.CREATED).body(coAo);
        } catch (Exception e) {
            logger.error("Error adding collar: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/addNSX")
    public ResponseEntity<NhaSanXuat> addManufacturer(@RequestBody laytamDTO laytamDTO) {
        if (laytamDTO == null) {
            return ResponseEntity.badRequest().body(null);
        }

        NhaSanXuat nhaSanXuat = new NhaSanXuat();
        nhaSanXuat.setTennsx(laytamDTO.getName());
        nhaSanXuat.setTrangthai(1);
        nhaSanXuat.setNgaytao(LocalDate.now());
        nhaSanXuat.setMansx(quenmatkhauService.random());
        try {
            sanPhamService.addNXS(nhaSanXuat);
            return ResponseEntity.status(HttpStatus.CREATED).body(nhaSanXuat);
        } catch (Exception e) {
            logger.error("Error adding manufacturer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/addDanhMuc")
    public ResponseEntity<DanhMuc> addCategory(@RequestBody laytamDTO laytamDTO) {
        if (laytamDTO == null) {
            return ResponseEntity.badRequest().body(null);
        }

        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setTendanhmuc(laytamDTO.getName());
        danhMuc.setNgaysua(LocalDate.now());
        danhMuc.setTrangthai(1);
        danhMuc.setMadanhmuc(quenmatkhauService.random());
        try {
            sanPhamService.addDanhmuc(danhMuc);
            return ResponseEntity.status(HttpStatus.CREATED).body(danhMuc);
        } catch (Exception e) {
            logger.error("Error adding category: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/addThuongHieu")
    public ResponseEntity<ThuongHieu> addBrand(@RequestBody laytamDTO laytamDTO) {
        if (laytamDTO == null) {
            return ResponseEntity.badRequest().body(null);
        }

        ThuongHieu thuongHieu = new ThuongHieu();
        thuongHieu.setTenthuonghieu(laytamDTO.getName());
        thuongHieu.setNgaysua(LocalDate.now());
        thuongHieu.setTrangthai(1);
        thuongHieu.setMathuonghieu(quenmatkhauService.random());
        try {
            sanPhamService.addthuonghieu(thuongHieu);
            return ResponseEntity.status(HttpStatus.CREATED).body(thuongHieu);
        } catch (Exception e) {
            logger.error("Error adding brand: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

        @PostMapping("/addTCSP")
        public ResponseEntity<?> addSanPham(@RequestBody sanPhamDTO sanPhamDTO) {
            try {
                // Gọi logic từ service để thêm sản phẩm
                sanPhamService.saveSanPham(sanPhamDTO);

                // Trả về phản hồi thành công với mã trạng thái 201 (Created)
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Sản phẩm đã được thêm thành công.");
            } catch (Exception e) {
                // Nếu có lỗi xảy ra, trả về phản hồi với mã trạng thái 500 (Internal Server Error)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Không thể thêm sản phẩm. Lỗi: " + e.getMessage());
            }
        }

    @GetMapping("/temporarySanPhamChiTietList")
    public ResponseEntity<List<SanPhamChiTiet>> getTemporarySanPhamChiTietList() {
        List<SanPhamChiTiet> list = sanPhamService.getTemporarySanPhamChiTietList();
        return ResponseEntity.ok(list);
    }








}





