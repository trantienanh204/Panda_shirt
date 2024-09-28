package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "KHACH_HANG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "MA_KHACH_HANG")
    private String makhachhang;

    @Column(name = "TEN_TAI_KHOAN")
    private String tentaikhoan;

    @Column(name = "MAT_KHAU")
    private String matkhau;

    @Column(name = "SO_DIEN_THOAI")
    private String sdt;

    @Column(name = "DIA_CHI_CU_THE")
    private String diachi;

    @Column(name = "TEN_KHACH_HANG")
    private String tenkhachhang;

    @Column(name = "TRANG_THAI")
    private int trangthai;

    @Column(name = "NGAY_TAO")
    private String ngaytao;

    @Column(name = "NGAY_SUA")
    private String ngaysua;


    @ManyToOne
    @JoinColumn(name = "ID_TINH_THANH_PHO")
    private Tinh_TP tinh_tp;

    @ManyToOne
    @JoinColumn(name = "ID_QUAN_HUYEN")
    private QuanHuyen quanHuyen;

    @ManyToOne
    @JoinColumn(name = "ID_XA_PHUONG")
    private XaPhuong xaPhuong;
}
