package com.example.demo.respository;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    @Query("SELECT hd FROM HoaDon hd WHERE " +
            "(?1 IS NULL OR hd.mahoadon LIKE %?1%) AND " +
            "(?2 IS NULL OR hd.nhanVien.tennhanvien LIKE %?2%) AND " +
            "(?3 IS NULL OR hd.khachHang.tenkhachhang LIKE %?3%) AND " +
            "(?4 IS NULL OR hd.trangthai = ?4)")
    Page<HoaDon> findByMaAndTenAndTrangthaiHD(String mahd, String tennv, String tenkh, Integer trangThai, Pageable pageable);

      HoaDon findTopByOrderByIdDesc();

    @Query("SELECT MAX(h.mahoadon) FROM HoaDon h")
    String findMaxMaHoaDon();

    @Query("SELECT h FROM HoaDon h WHERE h.khachHang IS NULL OR h.nhanVien IS NULL")
    List<HoaDon> findHoaDonsWithNullId();

}
