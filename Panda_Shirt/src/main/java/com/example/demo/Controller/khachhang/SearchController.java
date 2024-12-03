package com.example.demo.Controller.khachhang;

import com.example.demo.entity.DanhMuc;
import com.example.demo.entity.SanPham;
import com.example.demo.respository.DanhMucRepository;
import com.example.demo.service.DanhMucService;
import com.example.demo.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/panda")
public class SearchController {

    private final SanPhamService sanPhamService;
    private final DanhMucService danhMucService;

    @Autowired
    DanhMucRepository danhMucRepository;

    public SearchController(SanPhamService sanPhamService, DanhMucService danhMucService) {
        this.sanPhamService = sanPhamService;
        this.danhMucService = danhMucService;
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query,
                         @RequestParam(value = "category", required = false) String categoryId,
                         Model model) {
        List<SanPham> searchResults;
        if (categoryId == null || categoryId.isEmpty()) {
            searchResults = sanPhamService.searchSanPham(query);
        } else {
            searchResults = sanPhamService.searchSanPhamByCategory(query, Integer.parseInt(categoryId));
        }

        List<DanhMuc> categories = danhMucRepository.findAll();
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("categories", categories);
        return "khachhang/timkiemsanphamTT"; // Đảm bảo tên template trùng khớp
    }
}
