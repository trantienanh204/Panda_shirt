package com.example.demo.respository;

import com.example.demo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon,Integer> {
      HoaDon findTopByOrderByIdDesc();

    @Query("SELECT MAX(h.mahoadon) FROM HoaDon h")
    String findMaxMaHoaDon();

    @Query("SELECT h FROM HoaDon h WHERE h.khachHang IS NULL OR h.nhanVien IS NULL")
    List<HoaDon> findHoaDonsWithNullId();
}
