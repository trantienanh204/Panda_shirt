package com.example.demo.respository;

import com.example.demo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRespository extends JpaRepository<NhanVien,Integer> {
    boolean existsNhanVienByManhanvien(String manhanvien);

    NhanVien findByTentaikhoan(String name);

}
