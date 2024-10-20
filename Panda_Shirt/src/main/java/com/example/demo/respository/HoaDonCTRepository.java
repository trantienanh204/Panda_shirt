package com.example.demo.respository;

import com.example.demo.entity.HoaDonCT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonCTRepository extends JpaRepository<HoaDonCT,Integer> {
        @Query("SELECT hdct FROM HoaDonCT hdct WHERE hdct.hoaDon.id = :id")
        List<HoaDonCT> findhoadonct(@Param("id") int id);
}
