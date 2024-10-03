package com.example.demo.service;

import com.example.demo.Controller.repository.nhanvienRepository;
import com.example.demo.entity.NhanVien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private nhanvienRepository nhanvienRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NhanVien user = nhanvienRepository.findByTentaikhoan(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getTentaikhoan(),  // Tên tài khoản
                user.getMatkhau(),  // Mật khẩu đã mã hóa
                user.getTrangthai(),    // Kích hoạt tài khoản (boolean)
                true,                // accountNonExpired
                true,                // credentialsNonExpired
                true,                // accountNonLocked
                Collections.singletonList(new SimpleGrantedAuthority(user.getChucvu()))
        );
    }
    public void someMethod() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("Current logged in user: " + userDetails.getUsername());
            System.out.println("Authorities: " + userDetails.getAuthorities());
        }
    }


}
