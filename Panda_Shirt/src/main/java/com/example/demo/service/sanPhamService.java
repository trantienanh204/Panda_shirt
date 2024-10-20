
package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.respository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
import java.util.stream.Collectors;

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
        Sort sort = Sort.by(Sort.Direction.ASC,"id");

        Pageable pageable = PageRequest.of(page, size,sort);
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


    public SanPham convertToEntity(SanPhamDTO productDTO) {
        SanPham sanPham = new SanPham();

        // Gán các thuộc tính từ DTO vào Entity
        sanPham.setMasp(productDTO.getMasp());
        sanPham.setId(productDTO.getTenSanPham());
        int totalQuantity = productDTO.getChiTietSanPham().stream()
                .mapToInt(SanPhamChiTietDTO::getSoLuong)
                .sum();

// Gán tổng số lượng vào sản phẩm
        sanPham.setSoluongsp(totalQuantity);
        sanPham.setNgaytao(LocalDate.now());


        // Tìm các đối tượng từ database bằng ID
        DanhMuc danhMuc = danhMucRepository.findById(productDTO.getDanhMucId())
                .orElseThrow(() -> new EntityNotFoundException("DanhMuc không tìm thấy với ID: " + productDTO.getDanhMucId()));
        sanPham.setDanhMuc(danhMuc);

        ThuongHieu thuongHieu = thuongHieuRepository.findById(productDTO.getThuongHieuId())
                .orElseThrow(() -> new EntityNotFoundException("ThuongHieu không tìm thấy với ID: " + productDTO.getThuongHieuId()));
        sanPham.setThuongHieu(thuongHieu);

        ChatLieu chatLieu = chatLieuRespository.findById(productDTO.getChatLieuId())
                .orElseThrow(() -> new EntityNotFoundException("ChatLieu không tìm thấy với ID: " + productDTO.getChatLieuId()));
        sanPham.setChatLieu(chatLieu);

        NhaSanXuat nhaSanXuat = nsxRepository.findById(productDTO.getNhaSanXuatId())
                .orElseThrow(() -> new EntityNotFoundException("NhaSanXuat không tìm thấy với ID: " + productDTO.getNhaSanXuatId()));
        sanPham.setNhaSanXuat(nhaSanXuat);

        CoAo coAo = coAoRepository.findById(productDTO.getCoAoId())
                .orElseThrow(() -> new EntityNotFoundException("CoAo không tìm thấy với ID: " + productDTO.getCoAoId()));
        sanPham.setCoAo(coAo);

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
    public void saveSanPham(SanPhamDTO sanPhamDTO) {
        if (sanPhamDTO == null || sanPhamDTO.getTenSanPham() == null) {
            throw new IllegalArgumentException("Thông tin sản phẩm không hợp lệ!");
        }

        // Tìm sản phẩm theo tên sản phẩm
        SanPham sanPham = sanPhamRepository.findById(sanPhamDTO.getTenSanPham())
                .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tìm thấy với tên: " + sanPhamDTO.getTenSanPham()));

        int totalQuantity = sanPhamDTO.getChiTietSanPham() != null ?
                sanPhamDTO.getChiTietSanPham().stream().mapToInt(chitiet -> chitiet.getSoLuong() != null ? chitiet.getSoLuong() : 0).sum() : 0;
        sanPham.setSoluongsp(totalQuantity);

        // Cập nhật các thông tin khác
        DanhMuc danhMuc = danhMucRepository.findById(sanPhamDTO.getDanhMucId())
                .orElseThrow(() -> new EntityNotFoundException("Danh mục không tìm thấy với ID: " + sanPhamDTO.getDanhMucId()));
        sanPham.setDanhMuc(danhMuc);

        ThuongHieu thuongHieu = thuongHieuRepository.findById(sanPhamDTO.getThuongHieuId())
                .orElseThrow(() -> new EntityNotFoundException("Thương hiệu không tìm thấy với ID: " + sanPhamDTO.getThuongHieuId()));
        sanPham.setThuongHieu(thuongHieu);

        ChatLieu chatLieu = chatLieuRespository.findById(sanPhamDTO.getChatLieuId())
                .orElseThrow(() -> new EntityNotFoundException("Chất liệu không tìm thấy với ID: " + sanPhamDTO.getChatLieuId()));
        sanPham.setChatLieu(chatLieu);

        NhaSanXuat nhaSanXuat = nsxRepository.findById(sanPhamDTO.getNhaSanXuatId())
                .orElseThrow(() -> new EntityNotFoundException("Nhà sản xuất không tìm thấy với ID: " + sanPhamDTO.getNhaSanXuatId()));
        sanPham.setNhaSanXuat(nhaSanXuat);
        CoAo  coAo = coAoRepository.findById(sanPhamDTO.getCoAoId())
                .orElseThrow(() -> new EntityNotFoundException("Nhà sản xuất không tìm thấy với ID: " + sanPhamDTO.getCoAoId()));
        sanPham.setCoAo(coAo);

        // Cập nhật chi tiết sản phẩm
        if (sanPhamDTO.getChiTietSanPham() != null) {
            // Xóa các chi tiết sản phẩm cũ trước khi thêm mới
            sanPham.getSanPhamChiTietList().clear();
            sanPhamDTO.getChiTietSanPham().forEach(chiTietDTO -> {
                SanPhamChiTiet chiTiet = new SanPhamChiTiet();

                String colorInput = chiTietDTO.getMauSac();
                String color = colorInput.contains(":") ? colorInput.split(":")[1].trim() : colorInput;

                // Tìm kiếm MauSac từ cơ sở dữ liệu
                MauSac mauSac = mauSacRepsitory.findByTen(color)
                        .orElseThrow(() -> new EntityNotFoundException("Màu sắc không tìm thấy với tên: " + color));
                chiTiet.setMauSac(mauSac); // Thiết lập màu sắc vào chi tiết sản phẩm

                KichThuoc kichThuoc = kichThuocRepository.findByten(chiTietDTO.getKichThuoc())
                        .orElseThrow(() -> new EntityNotFoundException("Kích thước không tìm thấy với tên: " + chiTietDTO.getKichThuoc()));
                chiTiet.setKichThuoc(kichThuoc);

                chiTiet.setDongia(chiTietDTO.getGia());
                chiTiet.setSoluongsanpham(chiTietDTO.getSoLuong());
                chiTiet.setSanPham(sanPham);

                // Thêm chi tiết vào danh sách của sản phẩm
                sanPham.getSanPhamChiTietList().add(chiTiet);
            });
        }

        sanPhamRepository.save(sanPham);
    }



    public SanPham Listtimkiemsp(Integer id) {
        return  sanPhamRepository.findById(id).orElse(null);
    }
    public SanPhamChiTiet Listtimkiemspct(Integer id) {
        return  spctRepository.findById(id).orElse(null);
    }
    public MauSac Listtimkiemms(Integer id) {
        return  mauSacRepsitory.findById(id).orElse(null);
    }
    public KichThuoc Listtimkiemkt(Integer id) {
        return  kichThuocRepository.findById(id).orElse(null);
    }
    public void addspct(SanPhamChiTiet sanPhamChiTiet) {
        spctRepository.save(sanPhamChiTiet);
    }




}
