package com.example.demo.respository;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {

    boolean existsKhachHangByMakhachhang(String makhachhang);
    boolean existsKhachHangBySdt(String sdt);

    Optional<KhachHang> findByTentaikhoan(String name);

}
