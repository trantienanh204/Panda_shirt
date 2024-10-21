package com.example.demo.service;

import com.example.demo.entity.Voucher;
import com.example.demo.respository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VoucherService {
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    private EmailService voucherEmail;

    // Phương thức để cập nhật trạng thái voucher
    public void updateVoucherStatus() {
        LocalDate currentDate = LocalDate.now();
        List<Voucher> vouchers = voucherRepository.findAll();

        for (Voucher voucher : vouchers) {
            LocalDate startDate = voucher.getNgaybatdau();
            LocalDate endDate = voucher.getNgayketthuc();

            // Kiểm tra xem ngày bắt đầu và ngày kết thúc có hợp lệ không
            if (startDate != null && endDate != null) {
                if (currentDate.isBefore(startDate)) {
                    // Nếu ngày hiện tại bé hơn ngày bắt đầu, trạng thái là "Sắp hoạt động"
                    voucher.setTrangThai("Sắp hoạt động");
                } else if (currentDate.isAfter(endDate)) {
                    // Nếu ngày hiện tại sau ngày kết thúc, trạng thái cũng là "Hết hạn"
                    voucher.setTrangThai("Hết hạn");
                } else {
                    // Nếu ngày hiện tại nằm trong khoảng ngày bắt đầu đến ngày kết thúc, trạng thái là "Hoạt động"
                    voucher.setTrangThai("Đang hoạt động");
                }
                // Lưu lại voucher vào cơ sở dữ liệu
                voucherRepository.save(voucher);
            }
        }
    }

}
