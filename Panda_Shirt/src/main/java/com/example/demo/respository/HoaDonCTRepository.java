package com.example.demo.respository;

import com.example.demo.entity.HoaDonCT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface HoaDonCTRepository extends JpaRepository<HoaDonCT, Integer> {

    // 1. Doanh thu theo ngày
    @Query("SELECT COALESCE(SUM(hdct.soluong * hdct.dongia), 0) " +
            "FROM HoaDonCT hdct " +
            "WHERE hdct.hoaDon.trangthai = 1 AND hdct.ngaytao = :date")
    BigDecimal getRevenueByDate(@Param("date") LocalDate date);

    // 2. Doanh thu theo khoảng thời gian (ngày)
    @Query("SELECT hdct.ngaytao, COALESCE(SUM(hdct.soluong * hdct.dongia), 0) " +
            "FROM HoaDonCT hdct " +
            "WHERE hdct.hoaDon.trangthai = 1 AND hdct.ngaytao BETWEEN :startDate AND :endDate " +
            "GROUP BY hdct.ngaytao " +
            "ORDER BY hdct.ngaytao")
    List<Object[]> getDailyRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 3. Doanh thu theo tháng
    @Query("SELECT COALESCE(SUM(hdct.soluong * hdct.dongia), 0) " +
            "FROM HoaDonCT hdct " +
            "WHERE hdct.hoaDon.trangthai = 1 AND EXTRACT(YEAR FROM hdct.ngaytao) = :year AND EXTRACT(MONTH FROM hdct.ngaytao) = :month")
    BigDecimal getRevenueByMonth(@Param("year") int year, @Param("month") int month);

    // 4. Doanh thu theo năm
    @Query("SELECT COALESCE(SUM(hdct.soluong * hdct.dongia), 0) " +
            "FROM HoaDonCT hdct " +
            "WHERE hdct.hoaDon.trangthai = 1 AND EXTRACT(YEAR FROM hdct.ngaytao) = :year")
    BigDecimal getRevenueByYear(@Param("year") int year);
}

