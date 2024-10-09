<<<<<<< HEAD
package com.example.demo.Controller.sanpham;public class sanphanController {
}
=======
package com.example.demo.Controller.sanpham;

import com.example.demo.entity.Anh_SP;
import com.example.demo.entity.SanPham;
import com.example.demo.service.hinhanhService;
import com.example.demo.service.sanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
@RequestMapping("panda")
public class sanphanController {
    @Autowired
    private hinhanhService hinhanhService;
    @Autowired
 private sanPhamService sanPhamService;

    @GetMapping("/sanpham/list")
    public Page<SanPham> getSanPhamList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "tensp", required = false) String tensp,
            @RequestParam(value = "trangthai", required = false) Integer trangthai) {

        // Truyền tham số tên sản phẩm và trạng thái vào service
        return sanPhamService.hienThiSanPhamTheoDieuKien(page, tensp, trangthai);
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

    }



>>>>>>> detam
