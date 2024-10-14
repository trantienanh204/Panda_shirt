package com.example.demo.respository;

import com.example.demo.entity.DanhMuc;
import com.example.demo.entity.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu,Integer> {
    boolean existsThuongHieuByMathuonghieu(String ma);
    boolean existsThuongHieuByTenthuonghieu(String ten);

    Optional<ThuongHieu> findByTenthuonghieu(String name);



    ThuongHieu findByMathuonghieuAndIdNot(String man, Integer id);
    ThuongHieu findByTenthuonghieuAndIdNot(String ten, Integer id);
}
