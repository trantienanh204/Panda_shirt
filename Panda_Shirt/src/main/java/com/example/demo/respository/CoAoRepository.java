package com.example.demo.respository;

import com.example.demo.entity.CoAo;
import com.example.demo.entity.NhaSanXuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoAoRepository extends JpaRepository<CoAo,Integer> {
    boolean existsCoAoByMa(String ma);
    boolean existsCoAoByTen(String ten);
    Optional<CoAo> findByTen(String name);
}
