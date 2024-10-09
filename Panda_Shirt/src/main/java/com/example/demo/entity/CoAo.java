package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CO_AO")
public class CoAo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MA_CO_AO")
    @NotBlank(message = "Mã trống")
    private String ma;

    @Column(name = "TEN_CO_AO")
    @NotBlank(message = "Tên trống")
    private String ten;

    @Column(name = "NGAY_TAO")
    private LocalDate ngaytao;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaysua;

    @Column(name = "TRANG_THAI")
    private boolean trangthai;
}
