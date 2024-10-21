package com.example.demo.respository.nhanVien;

import com.example.demo.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
    @Query(value = "SELECT * FROM Don_Hang WHERE TRANG_THAI like N'%Chờ duyệt%'",nativeQuery = true)
    List<DonHang> listChoDuyet();
    @Query(value = "SELECT * FROM Don_Hang WHERE TRANG_THAI like N'%Đã duyệt%'",nativeQuery = true)
    List<DonHang> listDaDuyet();
    @Query(value = "SELECT * FROM Don_Hang WHERE TRANG_THAI like N'%Đã hủy%'",nativeQuery = true)
    List<DonHang> listDaHuy();
}
