package com.example.demo.service;

import com.example.demo.entity.SanPham;
import com.example.demo.respository.sanPhamRepository;
import org.hibernate.engine.jdbc.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class sanPhamService {
    @Autowired
    sanPhamRepository sanPhamRepository;

    private final int size = 5;

    public Page<SanPham> hienThiSanPhamTheoDieuKien(int page, String tensp, Integer trangthai) {
        Pageable pageable = PageRequest.of(page, size);
        return sanPhamRepository.findByTenspAndTrangthai(tensp, trangthai, pageable);
    }
}
