package com.example.demo.respository;

import com.example.demo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface nhanvienRepository extends JpaRepository<NhanVien,Integer> {
    public NhanVien findByTentaikhoan(String tennhanvien);
}
