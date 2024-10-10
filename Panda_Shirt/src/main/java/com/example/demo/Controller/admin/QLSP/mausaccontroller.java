package com.example.demo.Controller.admin.QLSP;

import com.example.demo.entity.MauSac;
import com.example.demo.respository.MauSacRepsitory;
import com.example.demo.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/panda/mausac")
public class MauSacController {
    @Autowired
    MauSacRepsitory mauSacRepsitory;
    @Autowired
    MauSacService mauSacService;
    @GetMapping("/hienthi")
    public String viewVC(Model model, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
//        slide bar
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
//        phân trang
        Pageable pageable = PageRequest.of(pageNo - 1, 3);
        Page<MauSac> listMS = mauSacRepsitory.findAll(pageable);
        model.addAttribute("totalPage", listMS.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("listms", listMS);
        model.addAttribute("MauSac", new MauSac());
        return "/admin/QLSP/MauSac";
    }

    @PostMapping("/add")
    public String AddVC(@ModelAttribute("MauSac") MauSac mauSac,Model model, RedirectAttributes redirectAttributes) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        String regex = "^[a-zA-Z0-9àáạảãâầấậẩăằắặẳêềếệểèẻẹéẽôồốộổơờớợởưừứựửữủũụúùìỉịíĩđòóỏọõ\\s]+$";

        Pattern pattern = Pattern.compile(regex);

        Matcher tenmsMatcher = pattern.matcher(mauSac.getMa());

        if(mauSac.getMa().isEmpty()){
            model.addAttribute("errorma","Không được để trống");
            return "admin/QLSP/ADD/AddMS";
        }
        if(mauSacRepsitory.existsMauSacByMa(mauSac.getMa())){
            model.addAttribute("errorma","Mã đã tồn tại");
            return "admin/QLSP/ADD/AddMS";
        }
        if(mauSac.getTen().isEmpty() || mauSac.getMa().isEmpty()){
            model.addAttribute("errorten","Không được để trống");
            return "admin/QLSP/ADD/AddMS";
        }else if (!tenmsMatcher.matches()) {
            model.addAttribute("errorten", "Tên chỉ được chứa chữ và số");
            return "admin/QLSP/ADD/AddMS";
        }
        if(mauSacRepsitory.existsMauSacByTen(mauSac.getTen())){
            model.addAttribute("errorten","Tên đã tồn tại");
            return "admin/QLSP/ADD/AddMS";
        }
        mauSac.setNgaytao(LocalDate.now());
        mauSacRepsitory.save(mauSac);
        redirectAttributes.addFlashAttribute("Add","Thêm thành công");


        return "/admin/QLSP/MauSac";
    }

    @GetMapping("/viewadd")
    public String view(Model model) {
        model.addAttribute("MauSac", new MauSac());
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        return "/admin/QLSP/ADD/AddMS";
    }

    @GetMapping("/remove/{id}")
    public String removePB(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        MauSac mauSac = mauSacRepsitory.findById(id).orElse(null);
        if (mauSac != null) {
            mauSac.setTrangthai(!mauSac.isTrangthai());
            mauSacRepsitory.save(mauSac);
            redirectAttributes.addFlashAttribute("UpdateStatusMessage", "Chuyển trạng thái thành công!");
        }
        return "redirect:/panda/mausac/hienthi";
    }


    @GetMapping("/update/{id}")
    public String detailPB(@PathVariable Integer id, Model model) {
        model.addAttribute("MauSac", new MauSac());
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        model.addAttribute("MauSac", mauSacRepsitory.getReferenceById(id));
        return "/admin/QLSP/UPDATE/UpdateMS";
    }

    @PostMapping("/update")
    public String update(Model model, @ModelAttribute("MauSac") MauSac mauSac,RedirectAttributes redirectAttributes) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);

        String tenms = mauSac.getTen().trim().toLowerCase();
        String ma = mauSac.getMa().trim().toLowerCase();

        String regex = "^[a-zA-Z0-9àáạảãâầấậẩăằắặẳêềếệểèẻẹéẽôồốộổơờớợởưừứựửữủũụúùìỉịíĩđòóỏọõ\\s]+$";
        String regexma = "^[a-zA-Z0-9]+$";

        Pattern pattern = Pattern.compile(regex);
        Pattern patternma = Pattern.compile(regexma);


        Matcher mamsMatcher = patternma.matcher(mauSac.getMa());
        Matcher tenmsMatcher = pattern.matcher(tenms);

        MauSac findma = mauSacRepsitory.findByMaAndIdNot(ma,mauSac.getId());
        MauSac findten = mauSacRepsitory.findByTenAndIdNot(tenms,mauSac.getId());

        if(mauSac.getMa().isEmpty()){
            model.addAttribute("errorma","Không được để trống");
            return "admin/QLSP/UPDATE/UpdateMS";
        }
        if(findma != null){
            model.addAttribute("errorma","Mã đã tồn tại");
            return "admin/QLSP/UPDATE/UpdateMS";
        }
        if(mauSac.getTen().isEmpty() || mauSac.getMa().isEmpty()){
            model.addAttribute("errorten","Không được để trống");
            return "admin/QLSP/UPDATE/UpdateMS";
        }else if (!tenmsMatcher.matches()) {
            model.addAttribute("errorten", "Tên chỉ được chứa chữ và số");
            return "admin/QLSP/UPDATE/UpdateMS";
        }
        if(findten != null){
            model.addAttribute("errorten","Tên đã tồn tại");
            return "admin/QLSP/UPDATE/UpdateMS";
        }
        mauSac.setNgaysua(LocalDate.now());
        mauSacRepsitory.save(mauSac);
        redirectAttributes.addFlashAttribute("Update", "Sửa thành công!");
        return "redirect:/panda/mausac/hienthi";
    }

}