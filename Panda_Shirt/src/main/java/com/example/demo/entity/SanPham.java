package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SAN_PHAM")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MA_SAN_PHAM")
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

    @Column(name = "Trang_thai")
    private Integer trangthai;

    @ManyToOne
    @JoinColumn(name = "ID_DANH_MUC",referencedColumnName = "id")
    private DanhMuc danhMuc;

    @ManyToOne
    @JoinColumn(name = "ID_NHA_SAN_XUAT",referencedColumnName = "id")
    private NhaSanXuat nhaSanXuat;

    @ManyToOne
    @JoinColumn(name = "ID_THUONG_HIEU",referencedColumnName = "id")
    private ThuongHieu thuongHieu;

    @ManyToOne
    @JoinColumn(name = "ID_CO_AO",referencedColumnName = "id")
    private CoAo coAo;

    @OneToOne
    @JoinColumn(name = "ID_CHAT_LIEU",referencedColumnName = "id")
    private ChatLieu chatLieu;


}
