package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "HOA_DON")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "MAHD")
    private String mahoadon;

    @Column(name = "SO_LUONG")
    private int soluong;

    @Column(name = "DON_GIA")
    private BigDecimal dongia;

    @Column(name = "SO_DIEN_THOAI")
    private String sdt;

    @Column(name = "NGAY_MUA")
    private LocalDate ngaymua;

    @Column(name = "NGAY_TAO")
    private LocalDate ngaytao;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaysua;

    @Column(name = "TONG_TIEN")
    private BigDecimal tongtien;

    @Column(name = "THANH_TIEN")
    private BigDecimal thanhtien;

    @Column(name = "TRANG_THAI")
    private int trangthai;

    @ManyToOne
    @JoinColumn(name = "ID_NHAN_VIEN",referencedColumnName = "id")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "ID_KHACH_HANG",referencedColumnName = "id")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ID_VOUCHER",referencedColumnName = "id")
    private Voucher voucher;
}
