//package com.example.demo.Controller.login;
//
//import com.example.demo.entity.NhanVien;
//import com.example.demo.respository.nhanvienRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    @Autowired
//   nhanvienRepository nhanvienRepository;
//    // mã hóa mật khẩu
//    @Bean
//    public PasswordEncoder getPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(getPasswordEncoder());
//
//        return authenticationManagerBuilder.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                NhanVien nv = nhanvienRepository.findByTentaikhoan(username)
//                        .orElseThrow(() -> new UsernameNotFoundException(username + " not found!"));
//
//                String password = nv.getMatkhau(); // Lấy mật khẩu
//                String[] roles = getRoles(nv); // Lấy vai trò
//                return User.withUsername(username)
//                        .password(password)
//                        .roles(roles)
//                        .build();
//            }
//            private String[] getRoles(NhanVien employee) {
//                // Lấy các vai trò dựa trên chức vụ
//                if (employee.getChucvu().equals("Quản lý")) {
//                    return new String[]{"QUANLY"};
//                }else if(employee.getChucvu().equals("Nhân viên")){
//                    return new String[]{"NHANVIEN"};
//                }else{
//                    return new String[]{"KHACHHANG"};
//                }
//            }
//        };
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http.csrf().disable().cors().disable();
//        http.authorizeHttpRequests(requests -> requests
//                .requestMatchers( "/panda/login","/panda/vaitro","/Image/**","panda/**").permitAll()
//                .requestMatchers("/panda/vaitro").hasAnyRole("QUANLY")
//                //.anyRequest().authenticated() // các url còn lại phải đăng nhập để sử dụng
//                .anyRequest().permitAll() // các url còn lại không cần đăng nhập vẫn có thể sử dụng
//        );
//        //http.exceptionHandling().accessDeniedPage("/auth/erro"); // url khi đăng nhập trái phép
//        http.formLogin(form -> form
//                .loginPage("/panda/login")  // Đường dẫn đến trang đăng nhập tùy chỉnh
//                .loginProcessingUrl("/panda/account")  // URL xử lý đăng nhập
//                //.defaultSuccessUrl("/panda/vaitro", false)  // Trang chuyển đến sau khi đăng nhập thành công
//                .failureUrl("/panda/account")  // Trang chuyển đến khi đăng nhập thất bại
//                .usernameParameter("username")  // Tên tham số cho trường username
//                .passwordParameter("password")  // Tên tham số cho trường password
//        );
////        http
////                .rememberMe()  // Kích hoạt tính năng "Remember Me"
////                .key("uniqueAndSecret")
////                .rememberMeParameter("remember"); // Tên của trường "Remember Me" trong form login
//        // Cấu hình logout
////        http.logout(logout -> logout
////                .logoutUrl("/auth/logout")  // URL để thực hiện logout
////                .logoutSuccessUrl("/auth/account")  // Trang chuyển đến sau khi logout thành công
////                .invalidateHttpSession(true) // Hủy bỏ session sau khi đăng xuất
////                .deleteCookies("JSESSIONID") // Xóa cookie của phiên làm việc
////                .addLogoutHandler(new SecurityContextLogoutHandler())  // Thêm handler cho logout
////        );
//        return http.build();
//    }
//
//}
package com.example.demo.Controller.login;

import com.example.demo.DTO.ChiTietVaiTroDTO;
import com.example.demo.DTO.TaiKhoanDTO;
import com.example.demo.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final String[] PUBLIC_ENDPOINTS = { "/panda/thongke","/panda/login","/panda/vaitro","/Image/**","panda/mahoa"};
    private final String[] QUANLY_ENDPOINTS= {"/panda/vaitro"};
    private final String[] NHANVIEN_ENDPOINTS= {"/panda/nsx"};
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
                                    .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage("/panda/login")
                            .failureUrl("/panda/login?error=true")
                            .successHandler( customAuthenticationSuccessHandler())
                            .usernameParameter("username")
                            .passwordParameter("password")
                    )
                    .logout(logoff -> logoff
                            .logoutUrl("/logoff")
                            .logoutSuccessUrl("/")
                            .permitAll()
                    )
                    .userDetailsService(userDetailsService(taiKhoanService));

            return http.build();
        }


        @Bean
        public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
            return (request, response, authentication) -> {
                // Lấy vai trò của người dùng
                Set<String> roles = AuthorityUtils
                        .authorityListToSet(authentication.getAuthorities());
                // In log để kiểm tra vai trò hiện tại của người dùng
                System.out.println("Các vai trò của người dùng: " + roles);

                // Điều hướng dựa trên vai trò
                if (roles.contains("ROLE_QUANLY")) {
                    System.out.println("Chuyển hướng đến /thongke");
                    response.sendRedirect("/panda/vaitro");
                } else if (roles.contains("ROLE_NHANVIEN")) {
                    System.out.println("Chuyển hướng đến");
                    response.sendRedirect("/panda/nsx");
                } else {
                    System.out.println("Chuyển hướng mặc định đến /khach-hang");
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
