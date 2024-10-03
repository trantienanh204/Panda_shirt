package com.example.demo.Controller;

import com.example.demo.service.test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panda")
public class LoginController {
@Autowired
 test test;

    @GetMapping("/login")
    public String login(){
        return "Login";
    }

}
