package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VOUCHER")
@Entity
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MA_KHUYEN_MAI")
    private String ma;

    @Column(name = "TEN_KHUYEN_MAI")
    private String ten;

    @Column(name = "SO_LUONG")
    private String soLuong;

    @Column(name = "MUC_GIAM")
    private String mucGiam;

    @Column(name = "MO_MA")
    private String mota;

    @Column(name = "LOAI")
    private boolean loai;

    @Column(name = "DIEU_KIEN_KHUYEN_MAI")
    private String dieuKien;


    @Column(name = "MA_AP_DUNG")
    private String maApDung;

    @Column(name = "NGAY_BAT_DAU")
    private LocalDate ngaybatdau;

    @Column(name = "NGAY_KET_THUC")
    private LocalDate ngayketthuc;

    @Column(name = "TRANG_THAI")
    private boolean trangThai;
}
