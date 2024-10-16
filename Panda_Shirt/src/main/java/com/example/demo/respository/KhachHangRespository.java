package com.example.demo.respository;

import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhachHangRespository extends JpaRepository<KhachHang,Integer> {
    @Query(value = "select * from KHACH_HANG where trang_thai = 1" ,nativeQuery = true)
    public List<KhachHang> dskhhoatdong();

}
