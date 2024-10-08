package com.example.demo.service;

import com.example.demo.entity.MauSac;
import com.example.demo.respository.MauSacRepsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MauSacService {
    @Autowired
    MauSacRepsitory mauSacRepsitory;
    public Optional<MauSac> finByMauSac(Integer ma_mau_sac) {
        return mauSacRepsitory.findById(ma_mau_sac);
    }

    public List<MauSac> getAllTrangThai() {
        return mauSacRepsitory.findAll();
    }
}
