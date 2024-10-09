package com.example.demo.Controller.admin.QLSP;

import com.example.demo.Repository.MauSacRepository;
import com.example.demo.Repository.SanPhamRepository;
import com.example.demo.Repository.SpctRepository;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/panda/sanpham")
public class SanPhamController {

    @Autowired
    SanPhamRepository sanPhamRepository;
    @Autowired
    SpctRepository spctRepository;

    @Autowired
    MauSacRepository mauSacRepository;

    SanPhamChiTiet spct = new SanPhamChiTiet();
    @GetMapping()
    public String hienthi(Model model){
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("lssanpham",sanPhamRepository.findAll());
        return "admin/QLSP/SanPham";
    }

    @GetMapping("showspct")
    public String showhienthi(Model model){
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("lssanpham",sanPhamRepository.findAll());
        return "admin/QLSP/CTSP";
    }

    @GetMapping("/ctsp")
    public String showspct(@RequestParam("id") int id, Model model){
        String role = "admin";
        model.addAttribute("role", role);
        model.addAttribute("sp",sanPhamRepository.findById(id).orElseThrow());

        List<MauSac> mauSacList = mauSacRepository.findAll();
        Map<String, SanPhamChiTiet> allSpctList = new HashMap<>(); // Sử dụng Map để giữ đối tượng duy nhất
        // Duyệt qua tất cả các màu sắc và in ra thông tin cho từng idMauSac
        for (MauSac mauSac : mauSacList) {
            Integer idMauSac = mauSac.getId();
            // Lấy danh sách SanPhamChiTiet với idSanPham và idMauSac
            List<SanPhamChiTiet> spctListnothinhanh = spctRepository.findBySanPhamAndMauSac(id, idMauSac);
            for (SanPhamChiTiet spct : spctListnothinhanh) {
                String key = spct.getSanPham().getId() + "-" + spct.getMauSac().getId();
                allSpctList.putIfAbsent(key, spct); // Chỉ thêm nếu chưa có khóa này
            }
            model.addAttribute("spctnot", new ArrayList<>(allSpctList.values()));
        }
        return "admin/QLSP/Anhspct";
    }

    @GetMapping("/change")
    public String change(@RequestParam("checkmausac") int idms,
                         @RequestParam("idsp") int idsp){
        List<SanPhamChiTiet> lsspct = spctRepository.findAll();
        for (SanPhamChiTiet spct : lsspct){
            if(spct.getSanPham().getId() == idsp && spct.getMauSac().getId() == idms){
                System.out.println("id sp : "+spct.getSanPham().getId());
                System.out.println("id ms : "+spct.getMauSac().getId());
                spct.setAnhsanpham(1);
                spctRepository.save(spct);
            }
        }
        return "redirect:/panda/sanpham";
    }

}
