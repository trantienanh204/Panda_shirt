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
@Table(name = "THUONG_HIEU")
public class ThuongHieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TEN_THUONG_HIEU")
    private String tenthuonghieu;


    @Column(name = "MA_THUONG_HIEU")
    private String mathuonghieu;

    @Column(name = "TRANG_THAI")
    private int trangthai;

    @Column(name = "NGAY_TAO",updatable = false,insertable = false)

    private LocalDate ngaytao;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaysua;
}
