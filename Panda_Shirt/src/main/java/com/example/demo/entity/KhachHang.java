package com.example.demo.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "KHACH_HANG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "MA_KHACH_HANG")
    private String makhachhang;
    @Size(max = 20, message = "Tên tài khoản từ 5 đến 20 ký tự")
    @Size(min = 5, message = "Tên tài khoản từ 5 đến 20 ký tự")
    @NotBlank(message = "Vui lòng nhập dữ liệu")
    @Column(name = "TEN_TAI_KHOAN")
    private String tentaikhoan;
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(max = 14, message = "Mật khẩu phải từ 8-14 ký tự")
    @Size(min = 8, message = "Mật khẩu phải từ 8-14 ký tự")
    @Column(name = "MAT_KHAU")
    private String matkhau;
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại phải bắt đầu bằng 0 và gồm 10 số")
    @Column(name = "SO_DIEN_THOAI")
    private String sdt;

    @Column(name = "DIA_CHI_CU_THE")
    private String diachi;

    @Column(name = "TEN_KHACH_HANG")
    private String tenkhachhang;

    @Column(name = "TRANG_THAI")
    private int trangthai;

    @Column(name = "NGAY_TAO")
    private String ngaytao;

    @Column(name = "NGAY_SUA")
    private String ngaysua;


    @ManyToOne
    @JoinColumn(name = "ID_TINH_THANH_PHO")
    private Tinh_TP tinh_tp;

    @ManyToOne
    @JoinColumn(name = "ID_QUAN_HUYEN")
    private QuanHuyen quanHuyen;

    @ManyToOne
    @JoinColumn(name = "ID_XA_PHUONG")
    private XaPhuong xaPhuong;
}
