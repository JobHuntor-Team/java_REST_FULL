package com.job.jobhunter.config;

import com.job.jobhunter.domain.Permission;
import com.job.jobhunter.domain.Role;
import com.job.jobhunter.domain.User;
import com.job.jobhunter.service.UserService;
import com.job.jobhunter.util.SecurityUtil;
import com.job.jobhunter.util.error.IdInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        // 1. Lấy Path Pattern (ví dụ: /api/v1/jobs/{id})
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();

        // 2. Kiểm tra điều kiện loại trừ để tránh lỗi 500 (Vòng lặp vô hạn)
        // Nếu không khớp pattern hoặc là đường dẫn lỗi hệ thống thì cho qua
        if (path == null || requestURI.startsWith("/error")) {
            return true;
        }

        System.out.println(">>> RUN PermissionInterceptor");
        System.out.println(">>> Path Pattern: " + path);
        System.out.println(">>> Method: " + httpMethod);

        // 3. Lấy email người dùng từ SecurityContext
        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        if (!email.isEmpty()) {
            User user = userService.handleGetUserByUserName(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();

                    // Kiểm tra danh sách quyền không được null
                    if (permissions == null || permissions.isEmpty()) {
                        throw new IdInvalidException("Tài khoản của bạn chưa được cấp quyền truy cập.");
                    }

                    // 4. So sánh Path Pattern với ApiPath trong Database
                    // Dùng trim() để loại bỏ khoảng trắng thừa gây lỗi logic
                    boolean isAllowed = permissions.stream()
                            .anyMatch(p -> p.getApiPath() != null &&
                                    p.getApiPath().trim().equals(path.trim()) &&
                                    p.getMethod() != null &&
                                    p.getMethod().equalsIgnoreCase(httpMethod));

                    if (!isAllowed) {
                        throw new IdInvalidException("Bạn không có quyền truy cập vào API này.");
                    }
                    System.out.println(">>> Quyền truy cập: HỢP LỆ");
                } else {
                    throw new IdInvalidException("Tài khoản của bạn chưa được gán vai trò (Role null).");
                }
            }
        }

        return true;
    }
}