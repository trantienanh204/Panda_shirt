package com.example.demo.Controller.admin.QLSP;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonCT;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NhanVien;
import com.example.demo.respository.*;
import com.example.demo.service.HoaDonService;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/panda/hoadon")
public class HoaDonCotroller {

    @Autowired
    HoaDonCTRepository hoaDonCTRepository;
    @Autowired
    HoaDonService hoaDonService;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    nhanvienRepository nhanvien;
    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    private TemplateEngine templateEngine; // Thymeleaf engine

    @ModelAttribute("listnhanvien")
    List<NhanVien> getnv (){return nhanvien.findAll();}

    @ModelAttribute("listkhachhang")
    List<KhachHang> getkh (){return khachHangRepository.findAll();}

    @GetMapping
    public String hienthi(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "mahd", required = false) String mahd,
                          @RequestParam(value = "tennv", required = false) String tennv,
                          @RequestParam(value = "tenkh", required = false) String tenkh,
                          @RequestParam(value = "trangThai", required = false) Integer trangThai,
                          Model model){
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);

        if (page < 0) {
            page = 0;
        }
        Page<HoaDon> listHD = hoaDonService.hienThiHD(page, mahd, tennv , tenkh, trangThai);
//        Page<HoaDon> listHD = hoaDonService.hienThiHD(page, mahd, tennv , tenkh, trangThai);
        model.addAttribute("totalPage", listHD.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("lshd",listHD.getContent());
        model.addAttribute("mahd", mahd);
        model.addAttribute("tennv", tennv);
        System.out.println("Tên nhân viên: " + tennv);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageSize", listHD.getSize());
        return "admin/HoaDon/HoaDon";
    }



    @GetMapping("update")
    public String formupdate(@RequestParam("id") int id,Model model){
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("hoadon",hoaDonRepository.findById(id).get());
        return "admin/HoaDon/UPDATE/UpdateHd";
    }

    @PostMapping("update")
    public String update(@ModelAttribute("hoadon")HoaDon hoaDon, Model model){
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        hoaDon.setTrangthai(1);
        hoaDonRepository.save(hoaDon);
        return "redirect:/panda/hoadon";
    }

    @GetMapping("import")
    public String importpdf(Model model){
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        List<HoaDonCT> lshdct  = hoaDonCTRepository.findhoadonct(1);
        model.addAttribute("hoadonct",lshdct);
        model.addAttribute("lshd",hoaDonRepository.findAll().get(0));
        System.out.println(lshdct .size());
        System.out.println(lshdct.get(0).getSanPhamChiTiet().getSanPham().getTensp());
        return "pdf";
    }
    @GetMapping("xuatfile")
    public String filePdf(@RequestParam("id") int id, Model model) {
        List<HoaDonCT> lshdct  = hoaDonCTRepository.findhoadonct(id);
        model.addAttribute("hoadonct",lshdct);

        String directoryPath = "D:\\HocTap\\HoaDon";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String customFileName = "hoadon_" + System.currentTimeMillis() + ".pdf";
        String filePath = directoryPath + "\\" + customFileName;

        List<HoaDon> hd = hoaDonRepository.findAll();
        model.addAttribute("hd", hd);

        Context context = new Context();
        context.setVariables(model.asMap());
        String htmlContent = templateEngine.process("pdf", context);

        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            HtmlConverter.convertToPdf(htmlContent, fos);
            return "pdf";
        } catch (IOException e) {
            e.printStackTrace();
            return "pdf"+ e.getMessage();
        }
    }
    @GetMapping("/chitiet")
    public String chiTietHoaDon(@RequestParam("id") Integer id, Model model) {
        List<HoaDonCT> hoaDonCT = hoaDonCTRepository.findhoadonct(id);
        model.addAttribute("hoaDonCTs", hoaDonCT);
        return "/nhanvien/DuyetDon :: chiTiet";
    }

}
