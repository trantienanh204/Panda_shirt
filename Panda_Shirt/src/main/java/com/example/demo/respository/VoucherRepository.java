package com.example.demo.respository;

import com.example.demo.entity.NhanVien;
import com.example.demo.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    boolean existsVoucherByMa(String ma);

    boolean existsVoucherByTen(String ten);

    Voucher findByMaAndIdNot(String ma, Integer id);

    Voucher findByTenAndIdNot(String ten, Integer id);


//    @Query(value = "SELECT * FROM Voucher vc WHERE " +
//            "(?1 IS NULL OR vc.ma_khuyen_mai LIKE %?1%) AND " +
//            "(?2 IS NULL OR vc.ten_khuyen_mai LIKE %?2%) AND " +
//            "(?3 IS NULL OR vc.trang_thai LIKE %?3%) AND " +
//            "(?4 IS NULL OR ?5 IS NULL OR " +
//            "(vc.ngay_bat_dau >= ?4 AND vc.ngay_bat_dau <= ?5 AND vc.ngay_ket_thuc >= ?4 AND vc.ngay_ket_thuc <= ?5)) " +
//            "ORDER BY vc.ngay_tao DESC",
//            nativeQuery = true)
//
@Query("SELECT vc FROM Voucher vc WHERE " +
        "(?1 IS NULL OR vc.ma LIKE CONCAT('%', ?1, '%')) AND " +
        "(?2 IS NULL OR vc.ten LIKE CONCAT('%', ?2, '%')) AND " +
        "(?3 IS NULL OR vc.ngaybatdau >= ?3) AND " +
        "(?4 IS NULL OR vc.ngayketthuc <= ?4) AND " +
        "(?5 IS NULL OR vc.trangThai LIKE CONCAT('%', ?5, '%')) " +
        "ORDER BY vc.ngaytao DESC")
Page<Voucher> findByMaAndTenAndTrangthaiVC(String ma, String ten, LocalDate startDate, LocalDate endDate, String trangThai, Pageable pageable);

    @Query(value = "SELECT * FROM voucher WHERE TRANG_THAI IN (N'Sắp hoạt động', N'Đang hoạt động') " +
            "ORDER BY NGAY_TAO DESC;", nativeQuery = true)
    List<Voucher> chonVC();
}
