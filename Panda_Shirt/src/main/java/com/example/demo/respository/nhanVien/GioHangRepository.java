package com.example.demo.respository.nhanVien;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
    public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
        Optional<GioHang> findByKhachHangAndSanPhamChiTiet(KhachHang khachHang, SanPhamChiTiet sanPhamChiTiet);
    List<GioHang> findByKhachHangId(int khachHangId);
    GioHang findByKhachHangIdAndSanPhamChiTietId(int khachHangId, int sanPhamChiTietId);
    List<GioHang> findAllByIdInAndKhachHangId(List<Integer> ids, int khachHangId);
    }


