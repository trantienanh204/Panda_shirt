package com.example.demo.Controller.nhanvien;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonCT;
import com.example.demo.entity.KhachHang;
import com.example.demo.respository.HoaDonCTRepository;
import com.example.demo.respository.HoaDonRepository;
import com.example.demo.service.HoaDonService;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/panda/nhanvien/xemhoadon")
public class XemHoaDonController {
    @Autowired
    HoaDonService hoaDonService;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    HoaDonCTRepository hoaDonCTRepository;
    @Autowired
    private TemplateEngine templateEngine;
    @GetMapping("/hienthi")
    public String hienthi(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "mahd", required = false) String mahd,
                          @RequestParam(value = "tennv", required = false) String tennv,
                          @RequestParam(value = "tenkh", required = false) String tenkh,
                          @RequestParam(value = "trangThai", required = false) Integer trangThai,
                          Model model){
        String role = "nhanvien"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);

        if (page < 0) {
            page = 0;
        }
        Page<HoaDon> listHD = hoaDonService.hienThiHD(page, mahd, tennv , tenkh, trangThai);
        model.addAttribute("totalPage", listHD.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("lshd",listHD.getContent());
        model.addAttribute("mahd", mahd);
        model.addAttribute("tennv", tennv);
        model.addAttribute("tenkh", tenkh);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageSize", listHD.getSize());
        List<HoaDonCT> listhdct = hoaDonCTRepository.findAll();
        model.addAttribute("listhdct",listhdct);
        return "/nhanvien/XemHoaDon";
    }
    @GetMapping("xuatfile")
    public String filePdf(@RequestParam("id") int id, Model model) {
        List<HoaDonCT> lshdct  = hoaDonCTRepository.findhoadonct(id);
        model.addAttribute("hoadonct",lshdct);
//        model.addAttribute("lshd",hoaDonRepository.findAll();

        String directoryPath = "D:\\HocTap\\HoaDon"; // Đường dẫn lưu PDF
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục nếu không tồn tại
        }
        String customFileName = "hoadon_" + System.currentTimeMillis() + ".pdf"; // Tên file tùy chỉnh
        String filePath = directoryPath + "\\" + customFileName; // Đường dẫn đầy đủ đến file PDF

//        String filePath = directoryPath + "\\hoadon.pdf"; // Đường dẫn đầy đủ đến file PDF

        // Lấy dữ liệu hóa đơn từ SQL
        List<HoaDon> hd = hoaDonRepository.findAll();
        model.addAttribute("hd", hd);

        // Render HTML từ Thymeleaf template
        Context context = new Context();
        context.setVariables(model.asMap());
        String htmlContent = templateEngine.process("pdf", context);

        // Chuyển HTML thành PDF
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
        return "/nhanvien/XemHoaDon :: chiTiet"; // Trả về fragment HTML
    }

}
