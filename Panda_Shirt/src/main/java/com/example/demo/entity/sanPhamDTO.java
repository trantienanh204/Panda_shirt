package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class sanPhamDTO {

    private String masp; // ID sản phẩm
    private Integer tenSanPham; // Tên sản phẩm
    private Integer danhMucId; // ID danh mục
    private Integer thuongHieuId; // ID thương hiệu
    private Integer chatLieuId; // ID chất liệu
    private Integer nhaSanXuatId; // ID nhà sản xuất
    private Integer coAoId; // ID cổ áo
    private List<SanPhamChiTietDTO> chiTietSanPham;

    @Override
    public String toString() {
        return "SanPhamDTO{" +
                "masp='" + masp + '\'' +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", danhMucId=" + danhMucId +
                ", thuongHieuId=" + thuongHieuId +
                ", chatLieuId=" + chatLieuId +
                ", nhaSanXuatId=" + nhaSanXuatId +
                ", coAoId=" + coAoId +
                ", chiTietSanPham=" + chiTietSanPham +
                '}';
    }


}

