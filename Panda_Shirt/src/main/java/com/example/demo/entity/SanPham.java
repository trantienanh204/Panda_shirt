package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SAN_PHAM")

//
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")


public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MA_SAN_PHAM", unique = true)
    private String masp;

    @Column(name = "TEN_SAN_PHAM")
    private String tensp;

    @Column(name = "ANH_SAN_PHAM")
    private String anhsp;

    @Column(name = "NGAY_TAO")
    private LocalDate ngaytao;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaysua;

    @Column(name = "SO_LUONG_SAN_PHAM")
    private Integer soluongsp;

    @Column(name = "TRANG_THAI")
    private Integer trangthai;

    @ManyToOne
    @JoinColumn(name = "ID_DANH_MUC", referencedColumnName = "id")
    private DanhMuc danhMuc;

    @ManyToOne
    @JoinColumn(name = "ID_NHA_SAN_XUAT", referencedColumnName = "id")
    private NhaSanXuat nhaSanXuat;

    @ManyToOne
    @JoinColumn(name = "ID_THUONG_HIEU", referencedColumnName = "id")
    private ThuongHieu thuongHieu;

    @ManyToOne
    @JoinColumn(name = "ID_CO_AO", referencedColumnName = "id")
    private CoAo coAo;

    @OneToOne
    @JoinColumn(name = "ID_CHAT_LIEU", referencedColumnName = "id")
    private ChatLieu chatLieu;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();


    @Transient
    private Double minPrice;

    public Double getMinPrice() {
        return sanPhamChiTietList.stream()
                .map(SanPhamChiTiet::getDongia)
                .min(Double::compareTo)
                .orElse(0.0);
    }



    public void addSanPhamChiTiet(SanPhamChiTiet chiTiet) {
        if (!this.sanPhamChiTietList.contains(chiTiet)) {
            chiTiet.setSanPham(this);
            this.sanPhamChiTietList.add(chiTiet);
        }
    }

    public void removeSanPhamChiTiet(SanPhamChiTiet chiTiet) {
        if (this.sanPhamChiTietList.contains(chiTiet)) {
            chiTiet.setSanPham(null);
            this.sanPhamChiTietList.remove(chiTiet);
        }
    }
}
