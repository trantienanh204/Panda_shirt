package com.example.demo.respository;

import com.example.demo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRespository extends JpaRepository<NhanVien,Integer> {
    boolean existsNhanVienByManhanvien(String manhanvien);
}
