package com.example.demo.respository;

import com.example.demo.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc,Integer> {
    boolean existsDanhMucByMadanhmuc(String ma);
    boolean existsDanhMucByTendanhmuc(String ten);

    DanhMuc findByMadanhmucAndIdNot(String ma, Integer id);
    DanhMuc findByTendanhmucAndIdNot(String ten, Integer id);
}
