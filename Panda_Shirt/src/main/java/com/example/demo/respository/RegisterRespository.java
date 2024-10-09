package com.example.demo.respository;

import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;
@Repository
=======
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
>>>>>>> detam
public interface RegisterRespository extends JpaRepository<KhachHang,Integer> {
}
