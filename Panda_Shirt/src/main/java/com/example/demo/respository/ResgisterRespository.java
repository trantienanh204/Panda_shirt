package com.example.demo.respository;

import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface ResgisterRespository extends JpaRepository<KhachHang,Integer> {
}
