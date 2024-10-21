package com.example.demo.respository;

import com.example.demo.entity.CoAo;
import com.example.demo.entity.KichThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KichThuocRepository extends JpaRepository<KichThuoc,Integer> {
    boolean existsKichThuocByMa(String ma);
    boolean existsKichThuocByTen(String ten);
    Optional<KichThuoc> findByten(String name);
    KichThuoc findByMaAndIdNot(String ma, Integer id);
    KichThuoc findByTenAndIdNot(String ten, Integer id);
}
