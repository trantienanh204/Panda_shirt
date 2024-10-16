package com.example.demo.Controller.login;

import com.example.demo.entity.NhanVien;

import com.example.demo.respository.nhanvienRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoginService {


    private final nhanvienRepository nhanvienRepository;

    public List<NhanVien> listk;

    public LoginService(nhanvienRepository nhanvienRepository) {
        this.nhanvienRepository = nhanvienRepository;
    }

    @PostConstruct
    public void init() {
        listk = nhanvienRepository.findAll();
    }
    public Optional<NhanVien> findUer(String username , String password){
        if (listk == null) {
            init();
        }
        Optional<NhanVien> user = listk.stream()
                .filter(u -> u.getTentaikhoan().equalsIgnoreCase(username) && u.getMatkhau().equalsIgnoreCase(password))
                .findFirst();
        return user;
    }

    public List<NhanVien> loginNV(String ten , String matkhau) {
        Optional<NhanVien> user =findUer(ten,matkhau);
        if(user.isPresent() && user.get().getChucvu().equals("Nhân viên")){
            List<NhanVien> tknv = new ArrayList<>();
            tknv.add(user.get());
            return tknv;
        }
        return new ArrayList<>();
    }

    public List<NhanVien> loginAdmin(String ten , String matkhau) {
        Optional<NhanVien> user =findUer(ten,matkhau);
        if(user.isPresent() && user.get().getChucvu().equalsIgnoreCase("Quản lý")){
            List<NhanVien> tkadmin = new ArrayList<>();
            tkadmin.add(user.get());
            return tkadmin;
        }
        return new ArrayList<>();
    }

}
