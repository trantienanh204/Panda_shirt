package com.example.demo.Controller.login;

import com.example.demo.DTO.ChiTietVaiTroDTO;
import com.example.demo.DTO.TaiKhoanDTO;
import com.example.demo.service.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = { "/panda/thongke","/panda/login","/Image/**","panda/mahoa","/panda/banhangoffline","/panda/giohang"};
    private final String[] QUANLY_ENDPOINTS= {};
    private final String[] NHANVIEN_ENDPOINTS= {};

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http, TaiKhoanService taiKhoanService) throws Exception {
            http
                    .cors()
                    .and()
                    .csrf().disable()
                    .authorizeHttpRequests(authorize -> authorize
                                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                                    .requestMatchers(QUANLY_ENDPOINTS).hasRole("QUANLY")
                                    .requestMatchers(NHANVIEN_ENDPOINTS).hasAnyRole("QUANLY","NHANVIEN")
//                        .requestMatchers("/").hasAnyRole("CUSTOMER")
//                                    .anyRequest().authenticated()
                                    .anyRequest().permitAll()
                    )
                    .formLogin(form -> form
                            .loginPage("/panda/login")
                            .failureUrl("/panda/login?error=true")
                            .successHandler( customAuthenticationSuccessHandler())
                            .usernameParameter("username")
                            .passwordParameter("password")
                    )
                    .logout(logoff -> logoff
                            .logoutUrl("/panda/logout")
                            .logoutSuccessUrl("/panda/login")
                            .permitAll()
                    )
                    .userDetailsService(userDetailsService(taiKhoanService));

            return http.build();
        }


    // Thêm vào lớp xử lý đăng nhập hoặc lớp bảo mật
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // Lấy vai trò của người dùng
            Set<String> roles = AuthorityUtils
                    .authorityListToSet(authentication.getAuthorities());

            // In log để kiểm tra vai trò hiện tại của người dùng
            System.out.println("Các vai trò của người dùng: " + roles);

            // Log thông tin session
            HttpSession session = request.getSession();
            System.out.println("Session ID: " + session.getId());

            // Điều hướng dựa trên vai trò
            if (roles.contains("ROLE_QUANLY")) {
                System.out.println("Chuyển hướng đến /panda/vaitro");
                response.sendRedirect("/panda/vaitro");
            } else if (roles.contains("ROLE_NHANVIEN")) {
                System.out.println("Chuyển hướng đến /panda/nhanvien/banhang/hienthi");
                response.sendRedirect("/panda/nhanvien/banhang/hienthi");
            } else {
                System.out.println("Chuyển hướng mặc định đến /panda/thongke");
                response.sendRedirect("/panda/thongke");
            }
        };
    }




    @Bean
        public UserDetailsService userDetailsService(TaiKhoanService taiKhoanService) {
            return tenDangNhap -> {
                TaiKhoanDTO taiKhoanDto = taiKhoanService.findByTenDangNhap(tenDangNhap);

                if (taiKhoanDto == null) {
                    throw new UsernameNotFoundException("Tài khoản không tồn tại: " + tenDangNhap);
                }

                // Kiểm tra trạng thái của nhân viên và khách hàng
                boolean isNhanVienActive = taiKhoanDto.getNhanVienDTO() != null && Boolean.TRUE.equals(taiKhoanDto.getNhanVienDTO().getTinhtrang());
                boolean isKhachHangActive = taiKhoanDto.getKhachHangDTO() != null && Boolean.TRUE.equals(taiKhoanDto.getKhachHangDTO().getDelete());
                System.out.println("isNhanVienActive: " + isNhanVienActive);
                System.out.println("isKhachHangActive: " + isKhachHangActive);

                if (!isNhanVienActive && !isKhachHangActive) {
                    throw new DisabledException("Tài khoản đã bị vô hiệu hóa");
                }

                // Lấy danh sách vai trò của tài khoản
                Set<ChiTietVaiTroDTO> roles = taiKhoanDto.getChiTietVaiTros();
                String[] roleNames = roles.stream()
                        .map(chiTietVaiTroDto -> chiTietVaiTroDto.getVaiTroDTO().getTenVaiTro())
                        .toArray(String[]::new);
                // Tạo đối tượng UserDetails, không khóa tài khoản khách hàng
                return User.withUsername(taiKhoanDto.getTenDangNhap())
                        .password(taiKhoanDto.getMatKhau())
                        .roles(roleNames)
                        .accountLocked(false) // Đảm bảo tài khoản không bị khóa đối với khách hàng
                        .build();
            };
        }

        @Bean
        public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(userDetailsService);
            provider.setPasswordEncoder(passwordEncoder);
            return provider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            //dùng để mã hóa mật khẩu
            return new BCryptPasswordEncoder();
        }
}
