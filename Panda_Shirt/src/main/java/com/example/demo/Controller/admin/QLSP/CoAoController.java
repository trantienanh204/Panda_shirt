//package com.example.demo.Controller.admin.QLSP;
//
//import com.example.demo.entity.ChatLieu;
//import com.example.demo.entity.CoAo;
//import com.example.demo.respository.CoAoRepository;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Controller
//@RequestMapping("/panda/coao")
//public class CoAoController {
//    @Autowired
//    CoAoRepository coAoRepository;
//
//    @GetMapping("")
//    public String index(Model model) {
//        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
//        model.addAttribute("role", role);
//        List<CoAo> list = coAoRepository.findAll();
//        model.addAttribute("list", list);
//        return "admin/QLSP/CoAo";
//    }
//
//    @GetMapping("/add")
//    public String showFormAdd(Model model) {
//        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
//        model.addAttribute("role", role);
//        model.addAttribute("coAo", new CoAo());
//        return "admin/QLSP/ADD/addCoAo";
//    }
//
//    @PostMapping("/add")
//    public String add(Model model, @Valid @ModelAttribute CoAo coAo, BindingResult result, RedirectAttributes redirectAttributes) {
//        String role = "admin";
//        model.addAttribute("role", role);
//
//        // Kiểm tra lỗi trong binding
//        if (result.hasErrors()) {
//            return "admin/QLSP/ADD/AddCoAo"; // Trả về trang thêm mới nếu có lỗi
//        }
//
//        // Kiểm tra mã đã tồn tại
//        if (coAoRepository.existsCoAoByMa(coAo.getMa())) {
//            model.addAttribute("errorma", "Mã đã tồn tại");
//            return "admin/QLSP/ADD/AddCoAo";
//        }
//
//        // Kiểm tra tên đã tồn tại
//        if (coAoRepository.existsCoAoByTen(coAo.getTen())) {
//            model.addAttribute("errorten", "Tên đã tồn tại");
//            return "admin/QLSP/ADD/AddCoAo";
//        }
//
//        // Nếu ID không phải là null, thực hiện cập nhật
//        if (coAo.getId() != null) {
//            CoAo existingCoAo = coAoRepository.findById(coAo.getId()).orElse(null);
//            if (existingCoAo != null) {
//                existingCoAo.setMa(coAo.getMa());
//                existingCoAo.setTen(coAo.getTen());
//                existingCoAo.setTrangThai(coAo.isTrangThai());
//                existingCoAo.setNgaySua(LocalDateTime.now());
//                coAoRepository.save(existingCoAo);
//            } else {
//                redirectAttributes.addFlashAttribute("ErrorStatusMessage", "Thêm không thành công!");
//                return "redirect:/panda/coao"; // Redirect sau khi có lỗi
//            }
//        } else {
//            // Thêm mới
//            coAo.setNgayTao(LocalDateTime.now());
//            coAo.setNgaySua(LocalDateTime.now());
//            coAoRepository.save(coAo);
//        }
//
//        // Thêm thông báo thành công
//        redirectAttributes.addFlashAttribute("AddStatusMessage", "Thêm thành công !");
//        return "redirect:/panda/coao";
//    }
//
//    @GetMapping("/update")
//    public String showFormUpdate(Model model, @RequestParam("id") Integer id) {
//        String role = "admin"; //Hoặc lấy giá trị role từ session hoặc service
//        model.addAttribute("role", role);
//        model.addAttribute("coAo", new CoAo());
//        model.addAttribute("coAo", coAoRepository.findById(id).get());
//        return "admin/QLSP/UPDATE/UpdateCoAo";
//    }
//
//    @PostMapping("/update")
//    public String update(@Validated @ModelAttribute CoAo coAo, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
//        String role = "admin";
//        model.addAttribute("role", role);
//        if (bindingResult.hasErrors()) {
//            return "admin/QLSP/UPDATE/UpdateCoAo";
//        }
//
//        // Nếu ID không phải là null, thực hiện cập nhật
//        if (coAo.getId() != null) {
//            CoAo existingCoAo = coAoRepository.findById(coAo.getId()).orElse(null);
//            if (existingCoAo != null) {
//                existingCoAo.setMa(coAo.getMa());
//                existingCoAo.setTen(coAo.getTen());
//                existingCoAo.setTrangThai(coAo.isTrangThai());
//                existingCoAo.setNgaySua(LocalDateTime.now());
//                coAoRepository.save(existingCoAo);
//            } else {
//                redirectAttributes.addFlashAttribute("ErrorStatusMessage", "Thêm không thành công!");
//                return "redirect:/panda/coao"; // Redirect sau khi có lỗi
//            }
//        } else {
//            // Thêm mới
//            coAo.setNgayTao(LocalDateTime.now());
//            coAo.setNgaySua(LocalDateTime.now());
//            coAoRepository.save(coAo);
//        }
//        redirectAttributes.addFlashAttribute("UpdateStatusMessage", "Cập nhật thành công !");
//        return "redirect:/panda/coao";
//    }
//    @GetMapping("/change")
//    public String changeStatus(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes) {
//        String role = "admin"; // Hoặc lấy giá trị role từ session hoặc service
//        model.addAttribute("role", role);
//
//        CoAo coAo = coAoRepository.findById(id).orElse(null);
//        if (coAo != null) {
//            coAo.toggleTrangThai(); // Đảo ngược trạng thái
//            coAoRepository.save(coAo);
//        }
//        redirectAttributes.addFlashAttribute("ChangesStatusMessage", "Chuyển trạng thái thành công !");
//        return "redirect:/panda/coao";
//    }
//}
