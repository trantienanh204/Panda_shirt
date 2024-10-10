package com.example.demo.Controller.login;


import com.example.demo.entity.NhanVien;
import com.example.demo.respository.nhanvienRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/panda")
public class LoginController {

    @Autowired
    nhanvienRepository nhanVienRepository ;

    NhanVien nv = new NhanVien();
    @Autowired
    LoginService loginService;


    @GetMapping("/account")
    public String login(@RequestParam("username") String username ,
                        @RequestParam("password") String password ,
                        Model model, HttpSession session
    ){

        Optional<NhanVien> user = loginService.findUer(username,password);
        String regex = "^[a-zA-Z0-9@._]*$";
        if(!username.matches(regex) || !password.matches(regex)){
            model.addAttribute("saitk","Tài khoản hoặc mật khẩu sai định dạng");
            System.out.printf("sai định dạng");
            return "Login";
        }
        if(user.isPresent()){
            List<NhanVien> tknv = loginService.loginNV(username,password);
            List<NhanVien> tkql = loginService.loginAdmin(username,password);
            if(!tknv.isEmpty()){
                session.setAttribute("usernv",tknv);
                session.removeAttribute("userql");
                System.out.printf("--tk : "+tknv.get(0).getChucvu());
                return "forward:/panda/nhanvien/banhang";
            }else if(!tkql.isEmpty()){
                session.setAttribute("userql",tkql);
                session.removeAttribute("usernv");
                System.out.printf("--tk : "+tkql.get(0).getChucvu());
                return "forward:/panda/vaitro";
            }
            return "Login";
        }
        model.addAttribute("saitk","Sai thông tin đăng nhập");
        System.out.printf("loi");
        return "Login";
    }

    @GetMapping("/login")
    public String account(){
        return "Login";
    }

}
