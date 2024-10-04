package com.example.demo.service;

import com.example.demo.respository.nhanvienRepository;
import com.example.demo.entity.NhanVien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class doimatkhauService {
    @Autowired
    nhanvienRepository nhanvienRepository;
    public NhanVien timkiemnhanvien(String taikhoan){
        return nhanvienRepository.findByTentaikhoan(taikhoan);
    }
    public void suanhanvien(NhanVien nhanVien){
        nhanvienRepository.save(nhanVien);
    }
}
