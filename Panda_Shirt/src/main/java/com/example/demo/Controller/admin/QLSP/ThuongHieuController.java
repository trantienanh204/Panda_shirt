package com.example.demo.Controller.admin.QLSP;

import com.example.demo.respository.ThuongHieuRepository;
import com.example.demo.entity.ThuongHieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/panda/thuonghieu")
public class ThuongHieuController {

    @Autowired
    ThuongHieuRepository thuongHieuRepository;

    ThuongHieu thuongHieu = new ThuongHieu();

    @GetMapping()
    public String danhmuc(Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("lsth", thuongHieuRepository.findAll());
        return "admin/QLSP/ThuongHieu";
    }

    @GetMapping("add")
    public String formadd(Model model) {
        String role = "admin";
        model.addAttribute("role", role);
        model.addAttribute( "thuonghieu", thuongHieu);
        return "admin/QLSP/ADD/AddThuongHieu";
    }

    @GetMapping("update")
    public String formupdate(@RequestParam("id") int id, Model model) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute( "thuonghieu", thuongHieuRepository.findById(id));
        return "admin/QLSP/UPDATE/UpdateThuongHieu";
    }

    @GetMapping("change")
    public String delete(@RequestParam("id") int id, Model model) {
        String role = "admin";
        model.addAttribute("role", role);
        ThuongHieu th = thuongHieuRepository.findById(id).orElse(null);
        if (th != null) {
            if (th.getTrangthai() == 1) {
                th.setTrangthai(0);
            } else {
                th.setTrangthai(1);
            }
            thuongHieuRepository.save(th);
        }
        return "redirect:/panda/thuonghieu";
    }


    @PostMapping("add")
    public String add(@ModelAttribute("thuonghieu") ThuongHieu thuongHieu , Model model) {
        String role = "admin";
        model.addAttribute("role", role);
        model.addAttribute( "nsx", thuongHieu);
        String regex = "^[a-zA-Z0-9àáạảãâầấậẩăằắặẳêềếệểèẻẹéẽôồốộổơờớợởưừứựửữủũụúùìỉịíĩđòóỏọõ\\s]+$";
        String regexma = "^[a-zA-Z0-9]+$";

        Pattern pattern = Pattern.compile(regex);
        Pattern patternma = Pattern.compile(regexma);

        Matcher tennsxMatcher = pattern.matcher(thuongHieu.getMathuonghieu());
        Matcher mansxMatcher = patternma.matcher(thuongHieu.getMathuonghieu());

        if(thuongHieu.getMathuonghieu().isEmpty()){
            model.addAttribute("errorma","Không được để trống");
            return "admin/QLSP/ADD/AddThuongHieu";
        }else if (!mansxMatcher.matches()) {
            model.addAttribute("errorma" ,"Mã chỉ được chứa chữ và số");
            return "admin/QLSP/ADD/AddThuongHieu";
        }
        if(thuongHieuRepository.existsThuongHieuByMathuonghieu(thuongHieu.getMathuonghieu())){
            model.addAttribute("errorma","Mã đã tồn tại");
            return "admin/QLSP/ADD/AddThuongHieu";
        }

        if(thuongHieu.getTenthuonghieu().isEmpty()){
            model.addAttribute("errorten","Không được để trống");
            return "admin/QLSP/ADD/AddThuongHieu";
        }else if (!tennsxMatcher.matches()) {
            model.addAttribute("errorten", "Tên chỉ được chứa chữ và số");
            return "admin/QLSP/ADD/AddThuongHieu";
        }
        if(thuongHieuRepository.existsThuongHieuByTenthuonghieu(thuongHieu.getTenthuonghieu())){
            model.addAttribute("errorten","Tên đã tồn tại");
            return "admin/QLSP/ADD/AddThuongHieu";
        }

        thuongHieuRepository.save(thuongHieu);
        return "redirect:/panda/thuonghieu";
    }

    @PostMapping("update")
    public String update(@ModelAttribute("thuonghieu") ThuongHieu thuongHieu , Model model) {
        String role = "admin";
        model.addAttribute("role", role);
        model.addAttribute( "nsx", thuongHieu);

        String ten = thuongHieu.getTenthuonghieu().trim().toLowerCase();
        String ma = thuongHieu.getMathuonghieu().trim().toLowerCase();

        String regex = "^[a-zA-Z0-9àáạảãâầấậẩăằắặẳêềếệểèẻẹéẽôồốộổơờớợởưừứựửữủũụúùìỉịíĩđòóỏọõ\\s]+$";
        String regexma = "^[a-zA-Z0-9]+$";

        Pattern pattern = Pattern.compile(regex);
        Pattern patternma = Pattern.compile(regexma);


        String tenth = thuongHieu.getTenthuonghieu().trim().toLowerCase();
        Matcher mansxMatcher = patternma.matcher(thuongHieu.getMathuonghieu());
        Matcher tennsxMatcher = pattern.matcher(tenth);

        ThuongHieu findma = thuongHieuRepository.findByMathuonghieuAndIdNot(ma,thuongHieu.getId());
        ThuongHieu findten = thuongHieuRepository.findByTenthuonghieuAndIdNot(ten,thuongHieu.getId());

        if(thuongHieu.getMathuonghieu().isEmpty()){
            model.addAttribute("errorma","Không được để trống");
            return "admin/QLSP/UPDATE/UpdateThuongHieu";
        }else if (!mansxMatcher.matches()) {
            model.addAttribute("errorma" ,"Mã chỉ được chứa chữ và số");
            return "admin/QLSP/UPDATE/UpdateThuongHieu";
        }
        if(findma != null){
            model.addAttribute("errorma","Mã đã tồn tại");
            return "admin/QLSP/UPDATE/UpdateThuongHieu";
        }

        if(thuongHieu.getTenthuonghieu().isEmpty()){
            model.addAttribute("errorten","Không được để trống");
            return "admin/QLSP/UPDATE/UpdateThuongHieu";
        }else if (!tennsxMatcher.matches()) {
            model.addAttribute("errorten", "Tên chỉ được chứa chữ và số");
            return "admin/QLSP/UPDATE/UpdateThuongHieu";
        }
        if(findten != null){
            model.addAttribute("errorten","Tên đã tồn tại");
            return "admin/QLSP/UPDATE/UpdateThuongHieu";
        }

        thuongHieu.setNgaysua(LocalDate.now());
        thuongHieuRepository.save(thuongHieu);
        return "redirect:/panda/thuonghieu";
    }
}