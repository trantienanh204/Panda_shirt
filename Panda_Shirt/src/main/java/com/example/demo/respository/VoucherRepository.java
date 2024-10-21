package com.example.demo.respository;

import com.example.demo.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Integer> {
    boolean existsVoucherByMa(String ma);
    boolean existsVoucherByTen(String ten);

    Voucher findVoucherByMa(String ma);
    Voucher findByMaAndIdNot(String ma, Integer id);
    Voucher findByTenAndIdNot(String ten, Integer id);

        @Query(value = "SELECT * FROM voucher WHERE TRANG_THAI IN (N'Sắp hoạt động', N'Đang hoạt động') " +
                "ORDER BY NGAY_TAO DESC;",nativeQuery = true)
    List<Voucher> listvc();

    @Query(value = "SELECT * FROM voucher ORDER BY NGAY_TAO DESC;",nativeQuery = true)
    Page<Voucher> listvoucher(Pageable pageable);


}
