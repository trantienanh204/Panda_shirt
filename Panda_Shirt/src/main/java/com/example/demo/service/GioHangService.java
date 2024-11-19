package com.example.demo.service;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.respository.KhachHangRepository;
import com.example.demo.respository.SanPhamChiTietRepository;
import com.example.demo.respository.nhanVien.GioHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

    @Service
    public class GioHangService {


        @Autowired
        private GioHangRepository gioHangRepository;

        @Autowired
        private SanPhamChiTietRepository sanPhamChiTietRepository;

        @Autowired
        private KhachHangRepository khachHangRepository;

        public GioHang addToCart(int khachHangId, int sanPhamChiTietId, int quantity) {
            KhachHang khachHang = khachHangRepository.findById(khachHangId)
                    .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(sanPhamChiTietId)
                    .orElseThrow(() -> new RuntimeException("Chi tiết sản phẩm không tồn tại"));
            Optional<GioHang> existingCartItem = gioHangRepository
                    .findByKhachHangAndSanPhamChiTiet(khachHang, sanPhamChiTiet);

            BigDecimal gia = BigDecimal.valueOf(sanPhamChiTiet.getDongia());

            if (existingCartItem.isPresent()) {
                GioHang cartItem = existingCartItem.get();
                cartItem.setSoluong(cartItem.getSoluong() + quantity);
                cartItem.setTongtien(gia.multiply(BigDecimal.valueOf(cartItem.getSoluong())).doubleValue());

                return gioHangRepository.save(cartItem);
            } else {
                GioHang newCartItem = new GioHang();
                newCartItem.setKhachHang(khachHang);
                newCartItem.setSanPhamChiTiet(sanPhamChiTiet);
                newCartItem.setSoluong(quantity);
                newCartItem.setTongtien(gia.multiply(BigDecimal.valueOf(newCartItem.getSoluong())).doubleValue());
                return gioHangRepository.save(newCartItem);
            }
        }


        public List<GioHang> getCartItems(int khachHangId) {
            return gioHangRepository.findByKhachHangId(khachHangId);
        }

        public void updateQuantity(int khachHangId, int sanPhamChiTietId, int quantity) { GioHang gioHang = gioHangRepository.findByKhachHangIdAndSanPhamChiTietId(khachHangId, sanPhamChiTietId); if (gioHang != null) { gioHang.setSoluong(quantity); gioHangRepository.save(gioHang); } else { throw new RuntimeException("Product not found in the cart."); } } public void deleteFromCart(int khachHangId, int sanPhamChiTietId) { GioHang gioHang = gioHangRepository.findByKhachHangIdAndSanPhamChiTietId(khachHangId, sanPhamChiTietId); if (gioHang != null) { gioHangRepository.delete(gioHang); } else { throw new RuntimeException("Product not found in the cart."); } }

        public void clearCart(int khachHangId) { List<GioHang> cartItems = gioHangRepository.findByKhachHangId(khachHangId);
            if (cartItems != null && !cartItems.isEmpty())
            { gioHangRepository.deleteAll(cartItems); } }
        public List<GioHang> getCartItemsByIds(int khachHangId, List<Integer> itemIds) { return gioHangRepository.findAllByIdInAndKhachHangId(itemIds, khachHangId); }

    }




