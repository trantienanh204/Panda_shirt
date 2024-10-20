package com.example.demo.services;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NhanVien;
import com.example.demo.respository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class KhachHangService {
    @Autowired
    KhachHangRepository khachHangRepository;

    // savetknhanvientodb
    public void saveCustomerToDb(MultipartFile file, KhachHang khachHang) {
        if (file != null && !file.isEmpty()) {
            try {
                // Lưu tệp hình ảnh vào đối tượng khách hàng
                khachHang.setImage(file.getBytes());
            } catch (IOException e) {
                System.err.println("Lỗi khi đọc tệp hình ảnh: " + e.getMessage());
                throw new RuntimeException("Không thể đọc tệp hình ảnh", e); // Ném lại ngoại lệ để controller có thể xử lý
            }
        } else {
            System.err.println("Tệp hình ảnh không hợp lệ hoặc rỗng.");
            throw new IllegalArgumentException("Tệp hình ảnh không hợp lệ hoặc rỗng");
        }

        // Lưu khách hàng vào cơ sở dữ liệu
        try {
            khachHangRepository.save(khachHang);
            System.out.println("Khách hàng đã được lưu thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu khách hàng: " + e.getMessage());
            throw new RuntimeException("Không thể lưu khách hàng", e); // Ném lại ngoại lệ để controller có thể xử lý
        }
    }

    // find by id
    public KhachHang findById(Integer id) {
        return khachHangRepository.findById(id).get();
    }
}
