
package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.respository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.engine.jdbc.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class sanPhamService {
    @Autowired
    sanPhamRepository sanPhamRepository;
    @Autowired
    ChatLieuRespository chatLieuRespository;
    @Autowired
    DanhMucRepository  danhMucRepository;
    @Autowired
    NSXRepository nsxRepository;
    @Autowired
    ThuongHieuRepository thuongHieuRepository;
    @Autowired
    SpctRepository  spctRepository;
    @Autowired
    CoAoRepository coAoRepository;
    @Autowired
    KichThuocRepository kichThuocRepository;
    @Autowired
    MauSacRepsitory  mauSacRepsitory ;
    private final int size = 5;

    public Page<SanPham> hienThiSanPhamTheoDieuKien(int page, String tensp, Integer trangthai) {
        Pageable pageable = PageRequest.of(page, size);
        return sanPhamRepository.findByTenspAndTrangthai(tensp, trangthai, pageable);
    }

    public List<SanPham> getallsp() {
        return sanPhamRepository.findAll();
    }
    public List<NhaSanXuat> getallNXS() {
        return nsxRepository.findAll();
    }
    public List<DanhMuc> getallDanhmuc() {
        return danhMucRepository.findAll();
    }
    public List<ChatLieu> getallCL() {
        return chatLieuRespository.findAll();
    }
    public List<ThuongHieu> getallthuonghieu() {
        return thuongHieuRepository.findAll();
    }
    public List<CoAo> getallcoao() {
        return coAoRepository.findAll();
    }
    public List<MauSac> getallmausac() {
        return mauSacRepsitory.findAll();
    }
    public List<KichThuoc> getallkichco() {
        return kichThuocRepository.findAll();
    }


    private List<SanPhamChiTiet> temporarySanPhamChiTietList = new ArrayList<>();

    public List<SanPhamChiTiet> geall() {
        System.out.println("Dữ liệu sản phẩm chi tiết tạm thời: ");
        for (SanPhamChiTiet chiTiet : temporarySanPhamChiTietList) {
            System.out.println(chiTiet); // In ra từng sản phẩm chi tiết
        }
        return temporarySanPhamChiTietList;
    }


    public SanPham convertToEntity(sanPhamDTO productDTO) {
        SanPham sanPham = new SanPham();

        // Gán các thuộc tính từ DTO vào Entity
        sanPham.setMasp(productDTO.getMasp());
        sanPham.setTensp(productDTO.getTenSanPham());
        sanPham.setSoluongsp(productDTO.getQuantity());
        sanPham.setNgaytao(productDTO.getCreatedDate());
        sanPham.setNgaysua(productDTO.getUpdatedDate());

        // Tìm các đối tượng từ database bằng tên
        Optional<DanhMuc> optionalDanhMuc = danhMucRepository.findByTendanhmucIgnoreCase(productDTO.getTenDanhMuc());
        if (optionalDanhMuc.isPresent()) {
            sanPham.setDanhMuc(optionalDanhMuc.get());
        } else {
            throw new EntityNotFoundException("DanhMuc không tìm thấy với tên: " + productDTO.getTenDanhMuc());
        }

        Optional<ThuongHieu> optionalThuongHieu = thuongHieuRepository.findByTenthuonghieu(productDTO.getTenThuongHieu());
        if (optionalThuongHieu.isPresent()) {
            sanPham.setThuongHieu(optionalThuongHieu.get());
        } else {
            throw new EntityNotFoundException("ThuongHieu không tìm thấy với tên: " + productDTO.getTenThuongHieu());
        }

        Optional<ChatLieu> optionalChatLieu = chatLieuRespository.findBytenChatLieu(productDTO.getTenChatLieu());
        if (optionalChatLieu.isPresent()) {
            sanPham.setChatLieu(optionalChatLieu.get());
        } else {
            throw new EntityNotFoundException("ChatLieu không tìm thấy với tên: " + productDTO.getTenChatLieu());
        }

        Optional<NhaSanXuat> optionalNhaSanXuat = nsxRepository.findByTennsx(productDTO.getTenNhaSanXuat());
        if (optionalNhaSanXuat.isPresent()) {
            sanPham.setNhaSanXuat(optionalNhaSanXuat.get());
        } else {
            throw new EntityNotFoundException("NhaSanXuat không tìm thấy với tên: " + productDTO.getTenNhaSanXuat());
        }

        Optional<CoAo> optionalCoAo = coAoRepository.findByTen(productDTO.getCoAo());
        if (optionalCoAo.isPresent()) {
            sanPham.setCoAo(optionalCoAo.get());
        } else {
            throw new EntityNotFoundException("CoAo không tìm thấy với tên: " + productDTO.getCoAo());
        }

        return sanPham;
    }


    public void createTemporarySanPhamChiTiet(SanPham sanPham, List<Integer> sizeIds, List<Integer> colorIds) {
        temporarySanPhamChiTietList.clear(); // Xóa danh sách cũ nếu có

        // Tính số lượng sản phẩm chi tiết được tạo ra
        int totalDetails = sizeIds.size() * colorIds.size();

        for (Integer sizeId : sizeIds) {
            for (Integer colorId : colorIds) {
                SanPhamChiTiet chiTiet = new SanPhamChiTiet();
                chiTiet.setSanPham(sanPham);

                // Tìm màu sắc và kích thước từ database bằng ID
                MauSac mauSac = mauSacRepsitory.findById(colorId).orElse(null);
                KichThuoc kichThuoc = kichThuocRepository.findById(sizeId).orElse(null);

                if (mauSac != null && kichThuoc != null) {
                    chiTiet.setMauSac(mauSac);
                    chiTiet.setKichThuoc(kichThuoc);
                    chiTiet.setSoluongsanpham(totalDetails); // Gán số lượng bằng số sản phẩm chi tiết được tạo ra

                    // Thêm vào danh sách tạm thời
                    temporarySanPhamChiTietList.add(chiTiet);
                }
            }
        }
    }

    public boolean updateTemporarySanPhamChiTiet(Long id, SanPhamChiTietDTO updatedProductDetail) {
        if (updatedProductDetail == null) {
            return false; // Không thực hiện nếu DTO là null
        }

        // Tìm chi tiết sản phẩm hiện có trong danh sách tạm
        SanPhamChiTiet existingProductDetail = temporarySanPhamChiTietList.stream()
                .filter(detail -> detail.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (existingProductDetail != null) {
            // Cập nhật số lượng và giá
            existingProductDetail.setSoluongsanpham(updatedProductDetail.getSoLuong());
            existingProductDetail.setDongia(updatedProductDetail.getGia());


            if (updatedProductDetail.getMauSac() != null) {
                Optional<MauSac> optionalMauSac = mauSacRepsitory.findByTen(updatedProductDetail.getMauSac());
                if (optionalMauSac.isPresent()) {
                    existingProductDetail.setMauSac(optionalMauSac.get()); // Cập nhật màu sắc
                } else {
                    throw new EntityNotFoundException("Màu sắc không tìm thấy với tên: " + updatedProductDetail.getMauSac());
                }
            }

// Cập nhật kích thước nếu có tên kích thước trong DTO
            if (updatedProductDetail.getKichThuoc() != null) {
                Optional<KichThuoc> optionalKichThuoc = kichThuocRepository.findByten(updatedProductDetail.getKichThuoc());
                if (optionalKichThuoc.isPresent()) {
                    existingProductDetail.setKichThuoc(optionalKichThuoc.get()); // Cập nhật kích thước
                } else {
                    throw new EntityNotFoundException("Kích thước không tìm thấy với tên: " + updatedProductDetail.getKichThuoc());
                }
            }


            return true; // Trả về true nếu cập nhật thành công
        }

        return false; // Trả về false nếu không tìm thấy sản phẩm chi tiết
    }






    public int saveTemporarySanPhamChiTiet() {
        int savedCount = 0; // Khởi tạo biến đếm số lượng sản phẩm đã lưu
        for (SanPhamChiTiet chiTiet : temporarySanPhamChiTietList) {
            spctRepository.save(chiTiet);
            savedCount++; // Tăng biến đếm mỗi khi lưu thành công
        }
        temporarySanPhamChiTietList.clear(); // Xóa danh sách tạm thời sau khi lưu
        return savedCount; // Trả về số lượng sản phẩm đã lưu
    }





    //    public List<SanPhamChiTiet> getTemporarySanPhamChiTietList() {
//        return temporarySanPhamChiTietList;
//    }
    public List<SanPhamChiTiet> getTemporarySanPhamChiTietList() {
        return temporarySanPhamChiTietList;
    }

    public boolean deleteTemporarySanPhamChiTiet(Long id) {
        // Kiểm tra xem sản phẩm có tồn tại không trước khi xóa
        boolean exists = temporarySanPhamChiTietList.stream().anyMatch(product -> product.getId().equals(id));
        if (exists) {
            temporarySanPhamChiTietList.removeIf(product -> product.getId().equals(id));
        }
        return exists; // Trả về true nếu xóa thành công, false nếu không tìm thấy
    }



    public void addsanpham(SanPham sanPham) {
        sanPhamRepository.save(sanPham);
    }

    public void addNXS(NhaSanXuat nhaSanXuat) {
        nsxRepository.save(nhaSanXuat);
    }

    public void addDanhmuc(DanhMuc danhMuc) {
        danhMucRepository.save(danhMuc);
    }

    public void addCL(ChatLieu chatLieu) {
        chatLieuRespository.save(chatLieu);
    }

    public void addthuonghieu(ThuongHieu thuongHieu) {
        thuongHieuRepository.save(thuongHieu);
    }

    public void addcoao(CoAo coAo) {
        coAoRepository.save(coAo);
    }

    @Transactional
    public void saveSanPham(sanPhamDTO sanPhamDTO) {

        SanPham sanPham = new SanPham();
        sanPham.setMasp(sanPhamDTO.getMasp());
        sanPham.setTensp(sanPhamDTO.getTenSanPham());
        sanPham.setSoluongsp(sanPhamDTO.getQuantity());
        sanPham.setNgaytao(LocalDate.now());
        sanPham.setNgaysua(LocalDate.now());

        // Tìm kiếm các thực thể từ các bảng khác
        DanhMuc danhMuc = danhMucRepository.findByTendanhmucIgnoreCase(sanPhamDTO.getTenDanhMuc())
                .orElseThrow(() -> new EntityNotFoundException("DanhMuc không tìm thấy!"));
        sanPham.setDanhMuc(danhMuc);

        ThuongHieu thuongHieu = thuongHieuRepository.findByTenthuonghieu(sanPhamDTO.getTenThuongHieu())
                .orElseThrow(() -> new EntityNotFoundException("ThuongHieu không tìm thấy!"));
        sanPham.setThuongHieu(thuongHieu);

        ChatLieu chatLieu = chatLieuRespository.findBytenChatLieu(sanPhamDTO.getTenChatLieu())
                .orElseThrow(() -> new EntityNotFoundException("ChatLieu không tìm thấy!"));
        sanPham.setChatLieu(chatLieu);

        NhaSanXuat nhaSanXuat = nsxRepository.findByTennsx(sanPhamDTO.getTenNhaSanXuat())
                .orElseThrow(() -> new EntityNotFoundException("NhaSanXuat không tìm thấy!"));
        sanPham.setNhaSanXuat(nhaSanXuat);

        // Tìm kiếm thực thể CoAo từ cơ sở dữ liệu
        CoAo coAo = coAoRepository.findByTen(sanPhamDTO.getCoAo())
                .orElseThrow(() -> new EntityNotFoundException("CoAo không tìm thấy!"));
        sanPham.setCoAo(coAo); // Thiết lập mối quan hệ với sản phẩm

        // Lưu sản phẩm vào cơ sở dữ liệu trước khi lưu chi tiết sản phẩm
        sanPhamRepository.save(sanPham);

        // Cập nhật thông tin chi tiết sản phẩm
        if (sanPhamDTO.getChiTietSanPham() != null && !sanPhamDTO.getChiTietSanPham().isEmpty()) {
            List<SanPhamChiTiet> chiTietList = new ArrayList<>();
            for (SanPhamChiTietDTO chiTietDTO : sanPhamDTO.getChiTietSanPham()) {
                SanPhamChiTiet chiTiet = new SanPhamChiTiet();

                // Tìm kiếm MauSac từ cơ sở dữ liệu
                MauSac mauSac = mauSacRepsitory.findByTen(chiTietDTO.getMauSac())
                        .orElseThrow(() -> new EntityNotFoundException("MauSac không tìm thấy!"));
                chiTiet.setMauSac(mauSac); // Thiết lập mauSac vào chi tiết sản phẩm

                // Tìm kiếm KichThuoc từ cơ sở dữ liệu
                KichThuoc kichThuoc = kichThuocRepository.findByten(chiTietDTO.getKichThuoc())
                        .orElseThrow(() -> new EntityNotFoundException("KichThuoc không tìm thấy!"));
                chiTiet.setKichThuoc(kichThuoc); // Thiết lập kichThuoc vào chi tiết sản phẩm

                chiTiet.setDongia(chiTietDTO.getGia());
                chiTiet.setSoluongsanpham(chiTietDTO.getSoLuong());
                chiTiet.setSanPham(sanPham); // Thiết lập mối quan hệ với sản phẩm

                chiTietList.add(chiTiet);
            }
            // Lưu tất cả các chi tiết sản phẩm vào cơ sở dữ liệu
            spctRepository.saveAll(chiTietList); // Sử dụng batch saving nếu có thể
        }
    }











}
