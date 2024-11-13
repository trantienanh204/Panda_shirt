package com.example.demo.respository;

import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet,Integer> {

    @Query("""
        SELECT sp FROM SanPhamChiTiet sp 
        WHERE LOWER(sp.sanPham.tensp) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<SanPhamChiTiet> findByTenSanPham(String keyword);
}
