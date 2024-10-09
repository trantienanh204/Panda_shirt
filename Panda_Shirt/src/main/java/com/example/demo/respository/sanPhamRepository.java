
package com.example.demo.respository;

import com.example.demo.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface sanPhamRepository extends JpaRepository<SanPham,Integer> {


        @Query("SELECT sp FROM SanPham sp WHERE " +
                "(?1 IS NULL OR sp.tensp LIKE %?1%) AND " +
                "(?2 IS NULL OR sp.trangthai = ?2)")
        Page<SanPham> findByTenspAndTrangthai(String tensp, Integer trangthai, Pageable pageable);



}
