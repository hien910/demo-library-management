package com.example.demolibrarymanagement.security;

import com.example.demolibrarymanagement.security.error.CustomAccessDeniedHandler;
import com.example.demolibrarymanagement.security.error.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityFilter {
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AuthenticationProvider authenticationProvider;
    private final DynamicAuthorityFilter dynamicAuthorityFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable); // Tắt CSRF

//        // Cấu hình form login
//        http.formLogin(formLogin -> {
//            formLogin.defaultSuccessUrl("/", true); // Nếu đăng nhập thành công thì chuyển hướng về trang chủ
//            formLogin.permitAll();
//        });
        // Cấu hình phân quyền
        http.authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers("/swagger-ui.html","/swagger-ui/**", "/v3/api-docs/**").permitAll();
//                    authorizeRequests.requestMatchers("").permitAll();
                    authorizeRequests.requestMatchers("POST","/api/user/login").permitAll();
                    authorizeRequests.requestMatchers("POST","/api/user/register").permitAll();
                    authorizeRequests.requestMatchers("POST", "/api/book").permitAll();
                    authorizeRequests.anyRequest().authenticated();
                }
        );
        // Cấu hình logout
        http.logout(logout -> {
            logout.clearAuthentication(true); // Xóa thông tin đăng nhập của user hiện tại trong SecurityContext
            logout.permitAll(); // Cho phép tất cả mọi người truy cập vào logout mà không cần xác thực
        });
        // Cấu hình xử lý exception
        http.exceptionHandling(exceptionHandling -> {
            exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint); // Xử lý khi chưa xác thực
            exceptionHandling.accessDeniedHandler(customAccessDeniedHandler); // Xử lý khi truy cập không đúng quyền
        });
        // Cấu hình xác thực
        http.authenticationProvider(authenticationProvider);
        // Cấu hình filter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(dynamicAuthorityFilter, JwtTokenFilter.class);
        return http.build();
    }
}