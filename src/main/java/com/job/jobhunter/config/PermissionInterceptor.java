package com.job.jobhunter.config;

import com.job.jobhunter.domain.Permission;
import com.job.jobhunter.domain.Role;
import com.job.jobhunter.domain.User;
import com.job.jobhunter.service.UserService;
import com.job.jobhunter.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        // check permission
        String email = SecurityUtil.getCurrentUserLogin() .isPresent()== true ?
                SecurityUtil.getCurrentUserLogin().get() : "";

        if(email != null || !email.isEmpty()) {
           User user = userService.handleGetUserByUserName(email);
            if (user != null) {
                Role role = user.getRole();
                if(role != null) {
                    List<Permission> permissions = role.getPermissions();
                    String regex = requestURI.replace("{id}", "\\d+");

                    boolean isAllowed = permissions.stream().anyMatch(item -> item.getApiPath().equals(path)
                            && item.getMethod().equals(httpMethod));
                    if (isAllowed == false) {
                        throw new IllegalArgumentException("bạn không có quyền truy cập vào API này");
                    }
                    System.out.println(">>> isAllowed= " + isAllowed);
                }
                else {
                    throw new IllegalArgumentException("role không hợp lệ");
                }
            }
        }

        return true;
    }
}