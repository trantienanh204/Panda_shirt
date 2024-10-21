package com.example.demo.respository;

import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.CoAo;
import com.example.demo.entity.NhaSanXuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NSXRepository extends JpaRepository<NhaSanXuat,Integer> {
    boolean existsNhaSanXuatByMansx(String ma);
    boolean existsNhaSanXuatByTennsx(String ten);

    Optional<NhaSanXuat> findByTennsx(String name);
    NhaSanXuat findByMansxAndIdNot(String mansx, Integer id);
    NhaSanXuat findByTennsxAndIdNot(String tennsx, Integer id);
}
