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

import com.example.demo.entity.NhanVien;
import com.example.demo.respository.nhanvienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private nhanvienRepository nhanvienRepository;


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService())
                .passwordEncoder(getPasswordEncoder());

        return authenticationManagerBuilder.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                NhanVien nv = nhanvienRepository.findByTentaikhoan(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username + " not found!"));

                String password = nv.getMatkhau(); // Lấy mật khẩu
                String[] roles = getRoles(nv); // Lấy vai trò
                return User.withUsername(username)
                        .password(password)
                        .roles(roles)
                        .build();
            }

            private String[] getRoles(NhanVien employee) {
                // Lấy các vai trò dựa trên chức vụ
                if (employee.getChucvu().equals("Quản lý")) {
                    return new String[]{"QUANLY"};
                } else if (employee.getChucvu().equals("Nhân viên")) {
                    return new String[]{"NHANVIEN"};
                } else {
                    return new String[]{"KHACHHANG"};
                }
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable().cors().disable();

        http.authorizeHttpRequests(requests -> requests
//<<<<<<< HEAD
//                .anyRequest().permitAll() // Cho phép tất cả các URL không cần xác thực
//=======
                .requestMatchers( "/panda/login","/panda/vaitro","/Image/**","panda/**","/panda/banhangoffline/**").permitAll()
                .requestMatchers("/panda/vaitro").hasAnyRole("QUANLY")
                //.anyRequest().authenticated() // các url còn lại phải đăng nhập để sử dụng
                .anyRequest().permitAll() // các url còn lại không cần đăng nhập vẫn có thể sử dụng

        );

        // Cấu hình đăng nhập
        http.formLogin(form -> form
                .loginPage("/panda/login")  // Đường dẫn đến trang đăng nhập tùy chỉnh
                .loginProcessingUrl("/panda/account")  // URL xử lý đăng nhập
                .failureUrl("/panda/account")  // Trang chuyển đến khi đăng nhập thất bại
                .usernameParameter("username")  // Tên tham số cho trường username
                .passwordParameter("password")  // Tên tham số cho trường password
        );

        return http.build();
    }

}
