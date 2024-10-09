package com.example.demo.Repository;

import com.example.demo.entity.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu,Integer> {
    boolean existsThuongHieuByMathuonghieu(String ma);
    boolean existsThuongHieuByTenthuonghieu(String ten);


    ThuongHieu findByMathuonghieuAndIdNot(String man, Integer id);
    ThuongHieu findByTenthuonghieuAndIdNot(String ten, Integer id);
}
