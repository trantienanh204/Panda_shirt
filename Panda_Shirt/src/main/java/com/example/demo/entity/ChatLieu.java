package com.example.demo.entity;

import jakarta.persistence.*;
<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
=======
>>>>>>> detam
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
<<<<<<< HEAD
import java.time.LocalDateTime;
=======
>>>>>>> detam

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CHAT_LIEU")
public class ChatLieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
    @Column(name = "ID")
    private int id;
    @NotBlank(message = "Không được để trống dữ liệu")
    @Size(min = 1, max = 50,message = "Mã phải có độ dài từ 1 đến 50 ký tự")
    @Column(name = "MA_CHAT_LIEU")
    private String maChatLieu;
    @NotBlank(message = "Không được để trống dữ liệu")
    @Size(min = 1, max = 50,message = "Tên phải có độ dài từ 1 đến 100 ký tự")
    @Column(name = "TEN_CHAT_LIEU")
    private String tenChatLieu;
    @Column(name = "NGAY_TAO")
    private LocalDateTime ngayTao;
    @Column(name = "NGAY_SUA")
    private LocalDateTime ngaySua;
    @NotNull(message = "Trạng thái không được để null")
    @Column(name = "TRANG_THAI")
    private boolean trangThai;

    public void toggleTrangThai() {
        this.trangThai = !this.trangThai; // Đảo ngược giá trị
    }
=======
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
>>>>>>> detam
}
