package com.example.demo.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoaDonCTDTO{
    private Integer id;
    private Integer idSanPhamCT;
    private Integer idHoadon;
    private String mahdct;
    private BigDecimal donGia;
    private Integer soLuong;
    private BigDecimal thanhTien;
    private String hinhthucthanhtoan;
}
