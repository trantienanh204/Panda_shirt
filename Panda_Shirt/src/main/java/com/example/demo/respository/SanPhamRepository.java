
package com.example.demo.respository;

import com.example.demo.entity.DanhMuc;
import com.example.demo.entity.SanPham;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham,Integer> {


                List<SanPham> findByTenspContainingIgnoreCase(String query);
                List<SanPham> findByTenspContainingIgnoreCaseAndDanhMucId(String query, int categoryId);



        @Query("SELECT sp FROM SanPham sp WHERE " +
                "(?1 IS NULL OR sp.tensp LIKE %?1%) AND " +
                "(?2 IS NULL OR sp.trangthai = ?2)"+
                "ORDER BY sp.ngaytao DESC")
        Page<SanPham> findByTenspAndTrangthai(String tensp, Integer trangthai, Pageable pageable);


        @Query("SELECT s FROM SanPham s WHERE s.sanPhamChiTietList IS NOT EMPTY")
        List<SanPham> findSanPhamWithDetails();


        List<SanPham> findByDanhMuc(DanhMuc danhMuc);


        List<SanPham> findAllByOrderByNgaytaoDesc();


//    @Query("SELECT sp FROM SanPham sp WHERE " +
//            "(?1 IS NULL OR sp.tensp LIKE %?1%) AND " +
//            "(?2 IS NULL OR sp.trangthai = ?2)"+
//            "ORDER BY sp.ngaytao DESC")
//    Page<SanPham> findByTenspAndTrangthai(String tensp, Integer trangthai, Pageable pageable);
//    @Query("SELECT s FROM SanPham s WHERE s.sanPhamChiTietList IS NOT EMPTY")
//    List<SanPham> findSanPhamWithDetails();
//    List<SanPham> findByDanhMuc(DanhMuc danhMuc);
//    List<SanPham> findAllByOrderByNgaytaoDesc();

                @Transactional
                @Modifying
                @Query("UPDATE SanPham s SET s.trangthai = :trangthai WHERE s.id = :sanPhamId" +
                        " AND NOT EXISTS" +
                        " (SELECT 1 FROM SanPhamChiTiet c WHERE c.sanPham.id = s.id AND c.soluongsanpham > 0)")
                void updateProductStatus(@Param("trangthai") Integer trangthai, @Param("sanPhamId") Integer sanPhamId);


}

