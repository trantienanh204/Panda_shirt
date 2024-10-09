package com.example.demo.respository;

import com.example.demo.entity.KichThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KichThuocRepository extends JpaRepository<KichThuoc,Integer> {
    boolean existsKichThuocByMa(String ma);
    boolean existsKichThuocByTen(String ten);

    KichThuoc findByMaAndIdNot(String ma, Integer id);
    KichThuoc findByTenAndIdNot(String ten, Integer id);
}
