package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SAN_PHAM_CHI_TIET")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "SO_LUONG_SAN_PHAM_CHI_TIET")
    private Integer soluongsanpham;

    @Column(name = "DON_GIA")
    private double dongia;

    @Column(name = "MO_TA")
    private String mota;

    @Column(name = "NGAY_TAO")
    private LocalDate ngaytao;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaysua;

    @Column(name = "TRANG_THAI")
    private boolean trangthai;

    @ManyToOne
    @JoinColumn(name = "ID_SAN_PHAM", referencedColumnName = "id")
////    @JsonIgnore // Ngăn không cho chuyển đổi đối tượng này thành JSON
////    @JsonBackReference
//
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "ID_KICH_THUOC")
    private KichThuoc kichThuoc;

    @ManyToOne
    @JoinColumn(name = "ID_MAU_SAC")
    private MauSac mauSac;

    public SanPhamChiTiet(Integer id) {
        this.id = id;
    }


    @Column(name = "ANH_SAN_PHAM_CHI_TIET")
    private byte[] anhSanPhamChiTiet;


    @Override
    public String toString() {
        return "SanPhamChiTiet{" +
                "id=" + id +
                ", soluongsanpham=" + soluongsanpham +
                ", dongia=" + dongia +
                ", mota='" + mota + '\'' +
                ", ngaytao=" + ngaytao +
                ", ngaysua=" + ngaysua +
                ", trangthai=" + trangthai +
//                ", sanPham=" + (sanPham != null ? sanPham.getId() : "null") +  // Hoặc lấy thuộc tính khác của SanPham
                ", kichThuoc=" + (kichThuoc != null ? kichThuoc.getId() : "null") +
                ", mauSac=" + (mauSac != null ? mauSac.getId() : "null") +
                '}';
    }

}
