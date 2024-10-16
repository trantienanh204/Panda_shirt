package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class sanPhamDTO {



                private String masp; // ID sản phẩm
                private String tenSanPham; // Tên sản phẩm
                private Integer quantity; // Số lượng sản phẩm
                private LocalDate createdDate; // Ngày tạo
                private LocalDate updatedDate; // Ngày sửa
                private String tenDanhMuc; // Tên danh mục
                private String tenThuongHieu; // Tên thương hiệu
                private String tenChatLieu; // Tên chất liệu
                private String tenNhaSanXuat; // Tên nhà sản xuất
                private String coAo; // Cổ áo

                // Danh sách kích thước và màu sắc
                private List<Integer> sizes; // Thêm danh sách kích thước
                private List<Integer> colors; // Thêm danh sách màu sắc

                // Danh sách sản phẩm chi tiết
                private List<SanPhamChiTietDTO> chiTietSanPham;




}

