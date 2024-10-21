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
@Table(name = "MAU_SAC")
public class MauSac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MA_MAU_SAC")

    private String ma;

    @Column(name = "TEN_MAU_SAC")
    private String ten;

    @Column(name = "NGAY_TAO", updatable = false,insertable = false)

    private LocalDate ngaytao;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaysua;

    @Column(name = "TRANG_THAI")
    private Integer trangthai;
}
