package com.example.demo.services.impl;

import com.example.demo.entity.KhachHang;
import com.example.demo.respository.ResgisterRespository;
import com.example.demo.services.ResgisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResgisterServiceimpl implements ResgisterService {
    @Autowired
    ResgisterRespository resgisterRespository;
    @Override
    public void add(KhachHang khachHang) {
        resgisterRespository.save(khachHang);
    }
}
