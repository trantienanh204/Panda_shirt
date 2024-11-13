
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
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
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
    @Autowired
    SanPhamChiTietRepository  sanPhamChiTietRepository ;
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


//    public SanPham convertToEntity(sanPhamDTO productDTO) {
//        SanPham sanPham = new SanPham();
//
//        // Gán các thuộc tính từ DTO vào Entity
//        sanPham.setMasp(productDTO.getMasp());
//        sanPham.setId(productDTO.getTenSanPham());
//        int totalQuantity = productDTO.getChiTietSanPham().stream()
//                .mapToInt(SanPhamChiTietDTO::getSoLuong)
//                .sum();
//
//// Gán tổng số lượng vào sản phẩm
//        sanPham.setSoluongsp(totalQuantity);
//        sanPham.setNgaytao(LocalDate.now());
//
//
//        // Tìm các đối tượng từ database bằng ID
//        DanhMuc danhMuc = danhMucRepository.findById(productDTO.getDanhMucId())
//                .orElseThrow(() -> new EntityNotFoundException("DanhMuc không tìm thấy với ID: " + productDTO.getDanhMucId()));
//        sanPham.setDanhMuc(danhMuc);
//
//        ThuongHieu thuongHieu = thuongHieuRepository.findById(productDTO.getThuongHieuId())
//                .orElseThrow(() -> new EntityNotFoundException("ThuongHieu không tìm thấy với ID: " + productDTO.getThuongHieuId()));
//        sanPham.setThuongHieu(thuongHieu);
//
//        ChatLieu chatLieu = chatLieuRespository.findById(productDTO.getChatLieuId())
//                .orElseThrow(() -> new EntityNotFoundException("ChatLieu không tìm thấy với ID: " + productDTO.getChatLieuId()));
//        sanPham.setChatLieu(chatLieu);
//
//        NhaSanXuat nhaSanXuat = nsxRepository.findById(productDTO.getNhaSanXuatId())
//                .orElseThrow(() -> new EntityNotFoundException("NhaSanXuat không tìm thấy với ID: " + productDTO.getNhaSanXuatId()));
//        sanPham.setNhaSanXuat(nhaSanXuat);
//
//        CoAo coAo = coAoRepository.findById(productDTO.getCoAoId())
//                .orElseThrow(() -> new EntityNotFoundException("CoAo không tìm thấy với ID: " + productDTO.getCoAoId()));
//        sanPham.setCoAo(coAo);
//
//        return sanPham;
//    }



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
            return false;
        }
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
                    existingProductDetail.setMauSac(optionalMauSac.get());
                } else {
                    throw new EntityNotFoundException("Màu sắc không tìm thấy với tên: " + updatedProductDetail.getMauSac());
                }
            }


            if (updatedProductDetail.getKichThuoc() != null) {
                Optional<KichThuoc> optionalKichThuoc = kichThuocRepository.findByten(updatedProductDetail.getKichThuoc());
                if (optionalKichThuoc.isPresent()) {
                    existingProductDetail.setKichThuoc(optionalKichThuoc.get());
                } else {
                    throw new EntityNotFoundException("Kích thước không tìm thấy với tên: " + updatedProductDetail.getKichThuoc());
                }
            }


            return true;
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






//    }
    public List<SanPhamChiTiet> getTemporarySanPhamChiTietList() {
        return temporarySanPhamChiTietList;
    }

    public boolean deleteTemporarySanPhamChiTiet(Long id) {

        boolean exists = temporarySanPhamChiTietList.stream().anyMatch(product -> product.getId().equals(id));
        if (exists) {
            temporarySanPhamChiTietList.removeIf(product -> product.getId().equals(id));
        }
        return exists;
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
        if (sanPhamDTO == null || sanPhamDTO.getTenSanPham() == null) {
            throw new IllegalArgumentException("Thông tin sản phẩm không hợp lệ!");
        }

        SanPham sanPham = sanPhamRepository.findById(sanPhamDTO.getTenSanPham())
                .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tìm thấy với tên: " + sanPhamDTO.getTenSanPham()));

        int totalQuantity = sanPhamDTO.getChiTietSanPham() != null
                ? sanPhamChiTietRepository.findBySanPhamId(sanPham.getId()).stream()
                .mapToInt(chitiet -> chitiet.getSoluongsanpham() != null ? chitiet.getSoluongsanpham() : 0)
                .sum()
                : 0;
        sanPham.setSoluongsp(totalQuantity);
        System.out.println("Tổng số lượng sản phẩm: " + totalQuantity);

        // Tìm các thông tin liên quan như danh mục, thương hiệu, chất liệu, nhà sản xuất, cổ áo
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

        CoAo coAo = coAoRepository.findById(sanPhamDTO.getCoAoId())
                .orElseThrow(() -> new EntityNotFoundException("Cổ áo không tìm thấy với ID: " + sanPhamDTO.getCoAoId()));
        sanPham.setCoAo(coAo);

        // Duyệt qua từng chi tiết sản phẩm trong DTO
        if (sanPhamDTO.getChiTietSanPham() != null) {
            List<SanPhamChiTiet> existingChiTietList = sanPham.getSanPhamChiTietList();

            sanPhamDTO.getChiTietSanPham().forEach(chiTietDTO -> {
                String colorInput = chiTietDTO.getMauSac();
                String color = colorInput.contains(":") ? colorInput.split(":")[1].trim() : colorInput;

                // Tìm màu sắc và kích thước
                MauSac mauSac = mauSacRepsitory.findByTen(color)
                        .orElseThrow(() -> new EntityNotFoundException("Màu sắc không tìm thấy với tên: " + color));
                KichThuoc kichThuoc = kichThuocRepository.findByten(chiTietDTO.getKichThuoc())
                        .orElseThrow(() -> new EntityNotFoundException("Kích thước không tìm thấy với tên: " + chiTietDTO.getKichThuoc()));

                SanPhamChiTiet existingChiTiet = existingChiTietList.stream()
                        .filter(chiTiet -> chiTiet.getMauSac().equals(mauSac) && chiTiet.getKichThuoc().equals(kichThuoc))
                        .findFirst()
                        .orElse(null);

                // Sử dụng images từ chiTietDTO
//                byte[] imageBytes = chiTietDTO.getImages();
//                if (imageBytes != null) {
//                    // Chuyển đổi mảng byte thành chuỗi base64
//                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//                    System.out.println("Base64 Image: " + base64Image);
//                } else {
//                    System.out.println("Không có dữ liệu hình ảnh.");
//                }
//                System.out.println("Đang kiểm tra hình ảnh: " + (imageBytes != null ? imageBytes.length : "null"));
//                if (imageBytes == null || imageBytes.length == 0) {
//                    throw new IllegalArgumentException("Hình ảnh không hợp lệ!");
//                }

                if (existingChiTiet != null) {
                    existingChiTiet.setSoluongsanpham(existingChiTiet.getSoluongsanpham() + chiTietDTO.getSoLuong());
                } else {
                    SanPhamChiTiet newChiTiet = new SanPhamChiTiet();
                    newChiTiet.setMauSac(mauSac);
                    newChiTiet.setKichThuoc(kichThuoc);
                    newChiTiet.setDongia(chiTietDTO.getGia());
                    newChiTiet.setSoluongsanpham(chiTietDTO.getSoLuong());
                    newChiTiet.setSanPham(sanPham);
                    newChiTiet.setAnhSanPhamChiTiet(null);
//                    System.out.println("Mã ảnh: " + (imageBytes != null ? imageBytes.length : "null"));
                    sanPham.getSanPhamChiTietList().add(newChiTiet);
                }
            });
        }
        sanPhamRepository.save(sanPham);
    }

    public Optional<SanPham> findSanPhamById(Integer id) {
        return sanPhamRepository.findById(id);
    }

    public Double[] getMinMaxPrice(Integer sanPhamId) {
        List<Object[]> result = sanPhamChiTietRepository.findMinAndMaxPriceBySanPhamId(sanPhamId);
        if (!result.isEmpty()) {
            Object[] prices = result.get(0);
            // Kiểm tra và chuyển đổi giá trị sang Double
            Double minPrice = prices[0] != null ? ((Number) prices[0]).doubleValue() : null;
            Double maxPrice = prices[1] != null ? ((Number) prices[1]).doubleValue() : null;
            return new Double[] { minPrice, maxPrice };
        }
        return new Double[] { null, null };
    }



    public List<MauSac> getColors(Integer sanPhamId) {
        return sanPhamChiTietRepository.findColorsBySanPhamId(sanPhamId);
    }
    public List<KichThuoc> getKichThuocs(Integer sanPhamId) {
        return sanPhamChiTietRepository.findkichThuocsBySanPhamId(sanPhamId);
    }

    public List<Map<String, Object>> getSizesAndColors(Integer sanPhamId) {
        List<Object[]> result = sanPhamChiTietRepository.findSizesAndColorsBySanPhamId(sanPhamId);
        List<Map<String, Object>> sizesAndColors = new ArrayList<>();

        for (Object[] row : result) {
            KichThuoc size = (KichThuoc) row[0];
            MauSac color = (MauSac) row[1];

            System.out.println("Size: " + size.getTen() + ", Color: " + color.getTen()); // Kiểm tra dữ liệu

            Map<String, Object> map = new HashMap<>();
            map.put("size", size);
            map.put("color", color);
            sizesAndColors.add(map);
        }

        return sizesAndColors;
    }

//    public List<Map<String, Object>> getSizesAndColorsWithPriceAndQuantity(Integer productId) {
//        return sanPhamChiTietRepository.getSizesAndColorsWithPriceAndQuantity(productId);
//    }
    public Double getPriceBySizeAndColor(Integer productId, Integer sizeId, Integer colorId) {
        // Giả sử bạn có bảng SanPhamChiTiet lưu thông tin về giá của sản phẩm theo kích thước và màu sắc.
        return sanPhamChiTietRepository.findPriceByProductSizeColor(productId, sizeId, colorId);
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
