package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SAN_PHAM_CHI_TIET")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ANH_SAN_PHAM_CHI_TIET")
    private String anhsanphamchitiet;

    @Column(name = "SO_LUONG_SAN_PHAM_CHI_TIET")
    private int soluongsanpham;

    @Column(name = "ID_HINH_ANH")
    private Integer anhsanpham;

    @Column(name = "DON_GIA")
    private Double dongia;

    @Column(name = "MO_TA")
    private String mota;

    @Column(name = "NGAY_TAO")
    private LocalDate ngaytao;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaysua;

    @Column(name = "TRANG_THAI")
    private boolean trangthai;

    @OneToOne
    @JoinColumn(name = "ID_SAN_PHAM")
    private SanPham sanPham;

    @OneToOne
    @JoinColumn(name = "ID_KICH_THUOC")
    private KichThuoc kichThuoc;

    @OneToOne
    @JoinColumn(name = "ID_MAU_SAC")
    private MauSac mauSac;


}
