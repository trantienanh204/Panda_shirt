package com.example.demo.respository;

import com.example.demo.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.NhanVien;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

import java.util.Optional;


@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

    boolean existsKhachHangByMakhachhang(String makhachhang);

    boolean existsKhachHangBySdt(String sdt);


    @Query("SELECT kh FROM KhachHang kh WHERE " +
            "(?1 IS NULL OR kh.makhachhang LIKE %?1%) AND " +
            "(?2 IS NULL OR kh.tenkhachhang LIKE %?2%) AND " +
            "(?3 IS NULL OR kh.trangthai = ?3)"+
            "ORDER BY kh.ngaytao DESC")
    Page<KhachHang> findByMaAndTenAndTrangthaiKH(String makh, String tenkh, Integer trangThai, Pageable pageable);

    @Query(value = "select * from KHACH_HANG where trang_thai = 1", nativeQuery = true)
    public List<KhachHang> dskhhoatdong();
//
//    @Query("SELECT kh.tinh_tp.tentinhTP FROM KhachHang kh WHERE kh.id = :id")
//    String findTenTinhByKhachHangId(@Param("id") Integer id);

    Optional<KhachHang> findByTentaikhoan(String name);


        @Query("SELECT kh FROM KhachHang kh WHERE kh.tentaikhoan = :tenTaiKhoan")
        Optional<KhachHang> findByTenTaiKhoan(@Param("tenTaiKhoan") String tenTaiKhoan);




}
