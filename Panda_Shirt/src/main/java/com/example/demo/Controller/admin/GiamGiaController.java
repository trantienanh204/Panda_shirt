package com.example.demo.Controller.admin;

import com.example.demo.entity.KichThuoc;
import com.example.demo.entity.Voucher;
import com.example.demo.respository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/panda/giamgia")
public class GiamGiaController {
    @Autowired
    VoucherRepository voucherRepository;
    @GetMapping("/hienthi")
    public String viewKT(Model model, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
        model.addAttribute("role", role);
        Pageable pageable = PageRequest.of(pageNo - 1, 3);
        Page<Voucher> listVC = voucherRepository.findAll(pageable);
        model.addAttribute("totalPage", listVC.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("listvc", listVC);
        model.addAttribute("Voucher", new Voucher());
        return "/admin/GiamGia";
    }
}
