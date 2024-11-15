package com.example.demo.service;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.HoaDon;
import com.example.demo.respository.nhanVien.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DonHangService {
    @Autowired
    DonHangRepository donHangRepository;
    private final int size = 5;

    public Page<DonHang> hienThiDH(int page, String mahd, String tennv, String tenkh, String trangThai) {
        if (page < 0) {
            throw new IllegalArgumentException("Chỉ số trang không được nhỏ hơn số không");
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return donHangRepository.findByMaAndTenAndDH(mahd, tennv, tenkh, trangThai, pageable);
    }

    public DonHang findID(Integer id) {
        return donHangRepository.findById(id).orElse(null);
    }
}
