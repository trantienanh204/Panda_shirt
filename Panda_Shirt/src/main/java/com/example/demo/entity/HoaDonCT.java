package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HOA_DON_CHI_TIET")
@Entity
public class HoaDonCT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "MAHDCT")
    private String mahoadonct;

    @Column(name = "SO_LUONG")
    private int soluong;

    @Column(name = "DON_GIA")
    private BigDecimal dongia;

    @Column(name = "NGAY_TAO")
    private LocalDate ngaytao;

    @Column(name = "TONG_TIEN")
    private BigDecimal tongtien;

    @Column(name = "HINH_THUC_THANH_TOAN")
    private String hinhthucthanhtoan;

    @ManyToOne
    @JoinColumn(name = "ID_SAN_PHAM",referencedColumnName = "id")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "ID_HOA_DON",referencedColumnName = "id")
    private HoaDon hoaDon;

}
