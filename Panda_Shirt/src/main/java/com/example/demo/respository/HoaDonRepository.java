package com.example.demo.respository;

import com.example.demo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon,Integer> {
    // Doanh thu theo ngày
    @Query("SELECT COALESCE(SUM(hd.tongtien), 0) " +
            "FROM HoaDon hd " +
            "WHERE hd.trangthai = 1 AND hd.ngaymua = :date")
    BigDecimal getRevenueByDate(@Param("date") LocalDate date);

    // Doanh thu theo từng ngày trong khoảng thời gian
    @Query("SELECT hd.ngaymua, COALESCE(SUM(hd.thanhtien), 0) " +
            "FROM HoaDon hd " +
            "WHERE hd.trangthai = 1 AND hd.ngaymua BETWEEN :startDate AND :endDate " +
            "GROUP BY hd.ngaymua " +
            "ORDER BY hd.ngaymua")
    List<Object[]> getDailyRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Doanh thu theo tháng
    @Query("SELECT COALESCE(SUM(hd.thanhtien), 0) " +
            "FROM HoaDon hd " +
            "WHERE hd.trangthai = 1 AND YEAR(hd.ngaymua) = :year AND MONTH(hd.ngaymua) = :month")
    BigDecimal getRevenueByMonth(@Param("year") int year, @Param("month") int month);

    // Doanh thu theo năm
    @Query("SELECT MONTH(hd.ngaymua), COALESCE(SUM(hd.thanhtien), 0) " +
            "FROM HoaDon hd " +
            "WHERE hd.trangthai = 1 AND YEAR(hd.ngaymua) = :year " +
            "GROUP BY MONTH(hd.ngaymua) " +
            "ORDER BY MONTH(hd.ngaymua)")
    List<Object[]> getMonthlyRevenueByYear(@Param("year") int year);

}
