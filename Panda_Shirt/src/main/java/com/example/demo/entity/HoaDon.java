package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "HOA_DON")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MAHD")
    private String mahoadon;

    @Column(name = "SO_LUONG")
    private int soluong;

    @Column(name = "DON_GIA")
    private BigDecimal dongia;

    @Column(name = "SO_DIEN_THOAI")
    private String sdt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    @Column(name = "DIA_CHI_CU_THE")
    private String diaChi;

//    @Column(name = "GHI_CHU")
//    private String ghiChu;

    @Column(name = "ACTIVE")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "ID_NHAN_VIEN", referencedColumnName = "id")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "ID_KHACH_HANG", referencedColumnName = "id")
    private KhachHang khachHang;


    @ManyToOne
    @JoinColumn(name = "ID_VOUCHER", referencedColumnName = "id")
    private Voucher voucher;

    public HoaDon(String mahoadon, int soluong, BigDecimal dongia, String sdt, LocalDate ngaymua, LocalDate ngaytao, LocalDate ngaysua, BigDecimal tongtien, BigDecimal thanhtien, int trangthai) {
        this.mahoadon = mahoadon;
        this.soluong = soluong;
        this.dongia = dongia;
        this.sdt = sdt;
        this.ngaymua = ngaymua;
        this.ngaytao = ngaytao;
        this.ngaysua = ngaysua;
        this.tongtien = tongtien;
        this.thanhtien = thanhtien;
        this.trangthai = trangthai;




//    @ManyToOne
//    @JoinColumn(name = "ID_VOUCHER",referencedColumnName = "id")
//    private Voucher voucher;


    }
    public HoaDon(Integer id) {
        this.id = id;

    }
}