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
@Table(name = "CHAT_LIEU")
public class ChatLieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "MA_CHAT_LIEU")
    private String machatlieu;
    @Column(name = "TEN_CHAT_LIEU")
    private String tenchatlieu;


    @Column(name = "NGAY_TAO",updatable = false,insertable = false)
    private LocalDate ngaytao;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaysua;

    @Column(name = "TRANG_THAI")
    private boolean trangthai;
}
