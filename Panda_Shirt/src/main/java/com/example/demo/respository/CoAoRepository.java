package com.example.demo.respository;

import com.example.demo.entity.CoAo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoAoRepository extends JpaRepository<CoAo,Integer> {
    boolean existsCoAoByMa(String ma);
    boolean existsCoAoByTen(String ten);
}
