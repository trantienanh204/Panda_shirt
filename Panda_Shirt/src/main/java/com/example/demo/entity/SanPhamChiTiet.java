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
//
//    @Column(name = "ANH_SAN_PHAM_CHI_TIET")
//
//    private String anh_san_pham_chi_tiet;

    @Column(name = "SO_LUONG_SAN_PHAM_CHI_TIET")
    private int so_luong_san_pham;

    @Column(name = "DON_GIA")
    private Double don_gia;

    @Column(name = "MO_TA")
    private String mo_ta;

    @Column(name = "NGAY_TAO")
    private LocalDate ngay_tao;

    @Column(name = "NGAY_SUA")
    private LocalDate ngay_sua;

    @Column(name = "TRANG_THAI")
    private boolean trang_thai;


    @OneToOne
    @JoinColumn(name = "ID_SAN_PHAM")
    private SanPham sanPham;

    @OneToOne
    @JoinColumn(name = "ID_KICH_THUOC")
    private KichThuoc kichThuoc;

    @OneToOne
    @JoinColumn(name = "ID_MAU_SAC")
    private MauSac mauSac;


    @OneToOne
    @JoinColumn(name = "ID_CHAT_LIEU")
    private ChatLieu chatLieu;


}
