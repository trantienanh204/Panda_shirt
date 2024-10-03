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
    private String ma_chat_lieu;
    @Column(name = "TEN_CHAT_LIEU")
    private String ten_chat_lieu;
    @Column(name = "NGAY_TAO")
    private LocalDate ngay_tao;
    @Column(name = "NGAY_SUA")
    private LocalDate ngay_sua;
    @Column(name = "TRANG_THAI")
    private boolean trang_thai;
}
