package com.example.demo.respository;

import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet,Integer> {
    @Query("SELECT hdct FROM SanPhamChiTiet hdct WHERE hdct.sanPham.id = :id")
    List<SanPhamChiTiet> findsanphamct(@Param("id") int id);
}
