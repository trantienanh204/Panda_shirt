package com.example.demo.respository;

import com.example.demo.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc,Integer> {
    boolean existsDanhMucByMadanhmuc(String ma);
    boolean existsDanhMucByTendanhmuc(String ten);
    public Optional<DanhMuc> findByTendanhmucIgnoreCase(String tendanhmuc);



    DanhMuc findByMadanhmucAndIdNot(String ma, Integer id);
    DanhMuc findByTendanhmucAndIdNot(String ten, Integer id);
}
