package com.example.demo.respository;


import com.example.demo.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MauSacRepsitory extends JpaRepository<MauSac,Integer> {
    List<MauSac> findAll();
    boolean existsMauSacByMa(String ma);
    boolean existsMauSacByTen(String ten);

    MauSac findByMaAndIdNot(String ma, Integer id);
    MauSac findByTenAndIdNot(String ten, Integer id);



}
