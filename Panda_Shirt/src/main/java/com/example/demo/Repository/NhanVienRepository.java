package com.example.demo.Repository;

import com.example.demo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien,Integer> {

//    @Query("""
//            SELECT nv from NhanVien nv WHERE nv.tentaikhoan =:username
//            """)
//    public NhanVien findByTentaikhoan(@Param("username") String username);

    Optional<NhanVien> findByTentaikhoan(String tentk);

}
