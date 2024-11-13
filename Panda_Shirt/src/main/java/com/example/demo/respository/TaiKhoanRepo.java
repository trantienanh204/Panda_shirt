package com.example.demo.respository;

import com.example.demo.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaiKhoanRepo extends JpaRepository<TaiKhoan,Integer> {

    TaiKhoan findByTenDangNhap(String ten);
}
