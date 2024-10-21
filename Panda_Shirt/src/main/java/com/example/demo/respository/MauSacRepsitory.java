package com.example.demo.respository;


import com.example.demo.entity.MauSac;
import com.example.demo.entity.NhaSanXuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MauSacRepsitory extends JpaRepository<MauSac,Integer> {
    List<MauSac> findAll();
    boolean existsMauSacByMa(String ma);
    boolean existsMauSacByTen(String ten);

    MauSac findByMaAndIdNot(String ma, Integer id);
    MauSac findByTenAndIdNot(String ten, Integer id);

    Optional<MauSac> findByTen(String name);

}
