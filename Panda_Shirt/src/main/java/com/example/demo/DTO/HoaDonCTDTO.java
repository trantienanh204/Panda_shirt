package com.example.demo.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoaDonCTDTO{
    private Integer idSanPhamCT;
    private Integer idHoadon;
    private BigDecimal donGia;
    private Integer soLuong;
    private BigDecimal thanhTien;
}
