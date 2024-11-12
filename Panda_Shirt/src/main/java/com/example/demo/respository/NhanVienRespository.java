package com.example.demo.respository;

import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRespository extends JpaRepository<NhanVien,Integer> {
    boolean existsNhanVienByManhanvien(String manhanvien);

    @Query("SELECT nv FROM NhanVien nv WHERE " +
            "(?1 IS NULL OR nv.manhanvien LIKE %?1%) AND " +
            "(?2 IS NULL OR nv.tennhanvien LIKE %?2%) AND " +
            "(?3 IS NULL OR nv.trangthai = ?3)")
    Page<NhanVien> findByMaAndTenAndTrangthaiNV(String manv, String tennv, Integer trangThai, Pageable pageable);

}
