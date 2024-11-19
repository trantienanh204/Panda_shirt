package com.example.demo.services;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.SanPham;
import com.example.demo.respository.HoaDonRepository;
import com.example.demo.respository.KhachHangRepository;

import com.example.demo.respository.sanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThongKeService {
    @Autowired
    sanPhamRepository sanPhamRepository;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    KhachHangRepository khachHangRepository;

    // Tìm ra tổng sản phẩm đang có và số lượng
    public List<Object[]> findProductNameAndQuantity() {
        List<Object[]> productData = sanPhamRepository.findAll().stream()
                .map(sp -> new Object[]{sp.getTensp(), sp.getSoluongsp()})
                .collect(Collectors.toList());

        // Log ra số lượng dữ liệu được lấy
        System.out.println("Số lượng sản phẩm lấy được: " + productData.size());

        return productData;
    }

    // Tính doanh thu
    public BigDecimal calculateTotalRevenue() {
        return hoaDonRepository.findAll().stream()
                .filter(hd -> hd.getTrangthai() == 1) // Lọc hoá đơn có trạng thái là 1
                .map(hd -> hd.getDongia().multiply(BigDecimal.valueOf(hd.getSoluong()))) // Tính doanh thu cho từng hóa đơn (Đơn giá * Số lượng)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Tính tổng doanh thu
    }

    // Doanh thu theo tháng
    public Map<Integer, BigDecimal> calculateMonthlyRevenue() {
        List<HoaDon> hoaDons = hoaDonRepository.findAll().stream()
                .filter(hd -> hd.getTrangthai() == 1) // Lọc hóa đơn có trạng thái là 1 (đã hoàn thành)
                .collect(Collectors.toList());

        Map<Integer, BigDecimal> monthlyRevenue = new HashMap<>();

        for (HoaDon hoaDon : hoaDons) {
            int month = hoaDon.getNgaymua().getMonthValue(); // Lấy tháng từ ngày lập hóa đơn
            BigDecimal revenueForInvoice = hoaDon.getDongia().multiply(BigDecimal.valueOf(hoaDon.getSoluong())); // Tính doanh thu của hóa đơn

            monthlyRevenue.merge(month, revenueForInvoice, BigDecimal::add);
        }

        return monthlyRevenue; // Trả về Map chứa doanh thu theo tháng
    }

    // Lọc đơn hàng thành công
    public long countSuccessfulProducts() {
        return hoaDonRepository.findAll().stream()
                .filter(sp -> sp.getTrangthai() == 1) // Lọc hóa đơn có trạng thái là 1
                .count();
    }

    // Đếm số lượng khách hàng
    public long countTotalCustomers() {
        return khachHangRepository.count();
    }

    // Thống kê doanh thu theo ngày
    public Map<LocalDate, BigDecimal> thongKeDoanhThuTheoNgay(LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        // Sử dụng LinkedHashMap để duy trì thứ tự ngày
        Map<LocalDate, BigDecimal> doanhThuTheoNgay = new LinkedHashMap<>();

        // Khởi tạo doanh thu mặc định là 0 cho mỗi ngày trong khoảng thời gian
        LocalDate ngayHienTai = ngayBatDau;
        while (!ngayHienTai.isAfter(ngayKetThuc)) {
            doanhThuTheoNgay.put(ngayHienTai, BigDecimal.ZERO);
            ngayHienTai = ngayHienTai.plusDays(1);
        }

        // Lấy danh sách hóa đơn từ cơ sở dữ liệu
        List<HoaDon> danhSachHoaDon = hoaDonRepository.findAll();
        for (HoaDon hoaDon : danhSachHoaDon) {
            if (hoaDon.getTrangthai() == 1) {
                LocalDate ngayMua = hoaDon.getNgaymua();

                if (!ngayMua.isBefore(ngayBatDau) && !ngayMua.isAfter(ngayKetThuc)) {
                    BigDecimal doanhThu = hoaDon.getDongia().multiply(BigDecimal.valueOf(hoaDon.getSoluong()));
                    doanhThuTheoNgay.put(ngayMua, doanhThuTheoNgay.get(ngayMua).add(doanhThu));
                }
            }
        }

        return doanhThuTheoNgay;
    }

    // Doanh thu theo ngày (cộng dồn tất cả hóa đơn trong ngày)
    public List<Map<String, Object>> getDailyRevenue(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> revenueList = new ArrayList<>();

        // Lấy danh sách hóa đơn thành công trong khoảng thời gian từ startDate đến endDate
        List<HoaDon> hoaDons = hoaDonRepository.findAll().stream()
                .filter(hd -> hd.getTrangthai() == 1) // Lọc hóa đơn có trạng thái là 1 (thành công)
                .filter(hd -> !hd.getNgaymua().isBefore(startDate) && !hd.getNgaymua().isAfter(endDate)) // Lọc theo khoảng thời gian
                .collect(Collectors.toList());

        // Duyệt qua các ngày từ startDate đến endDate
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            Map<String, Object> revenueData = new HashMap<>();
            revenueData.put("date", currentDate.toString()); // Gán ngày hiện tại vào map

            // Tính tổng doanh thu cho ngày hiện tại
            LocalDate finalCurrentDate = currentDate;
            BigDecimal totalRevenueForDay = hoaDons.stream()
                    .filter(hd -> hd.getNgaymua().equals(finalCurrentDate)) // Chỉ lấy hóa đơn của ngày hiện tại
                    .map(hd -> hd.getDongia().multiply(BigDecimal.valueOf(hd.getSoluong()))) // Tính doanh thu cho mỗi hóa đơn (Đơn giá * Số lượng)
                    .reduce(BigDecimal.ZERO, BigDecimal::add); // Cộng dồn doanh thu cho tất cả hóa đơn trong ngày

            revenueData.put("revenue", totalRevenueForDay); // Thêm doanh thu của ngày vào dữ liệu

            revenueList.add(revenueData); // Thêm dữ liệu vào danh sách

            currentDate = currentDate.plusDays(1); // Chuyển sang ngày kế tiếp
        }

        return revenueList; // Trả về danh sách doanh thu theo ngày
    }
    public List<Map<String, Object>> getRevenueByYear(int year) {
        List<Map<String, Object>> revenueList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            BigDecimal monthlyRevenue = hoaDonRepository.getRevenueByMonth(year, month);
            Map<String, Object> revenueData = new HashMap<>();
            revenueData.put("month", "Tháng " + month);
            revenueData.put("revenue", monthlyRevenue != null ? monthlyRevenue : BigDecimal.ZERO);
            revenueList.add(revenueData);
        }

        return revenueList;
    }

}
