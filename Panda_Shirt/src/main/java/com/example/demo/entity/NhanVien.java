package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "NHAN_VIEN")
@Data
@NoArgsConstructor
@AllArgsConstructor


@SuppressWarnings("serial")
public class NhanVien implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "TEN_NHAN_VIEN")
    private String tennhanvien;

    @Column(name = "MA_NHAN_VIEN")
    private String manhanvien;

    @Column(name = "TEN_TAI_KHOAN")
    private String tentaikhoan;

    @Column(name = "MAT_KHAU")
    private String matkhau;

    @Column(name = "CHUC_VU")
    private String chucvu;

    @Column(name = "TRANG_THAI")


    private int trangthai;


    @Column(name = "NGAY_TAO")
    private String ngaytao;

    @Column(name = "NGAY_SUA")
    private String ngaysua;

    @Column(name = "GIOI_TINH")
    private String gioitinh;

    @JsonIgnore
    @OneToMany(mappedBy = "nhanVien")
    List<UserRole> userRoles;


}
