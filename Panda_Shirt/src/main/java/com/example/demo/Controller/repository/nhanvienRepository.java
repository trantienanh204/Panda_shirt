package com.example.demo.Controller.repository;

import com.example.demo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface nhanvienRepository extends JpaRepository<NhanVien,Integer> {
    public NhanVien findByTentaikhoan(String tennhanvien);
}
