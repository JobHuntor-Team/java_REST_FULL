package com.job.jobhunter.controller;

import com.job.jobhunter.domain.User;
import com.job.jobhunter.domain.response.ResLoginDTO;
import com.job.jobhunter.domain.response.ResLoginDTO.UserLogin;
import com.job.jobhunter.domain.response.UserCreateDTO;
import com.job.jobhunter.domain.resquest.ReqLoginDTO;
import com.job.jobhunter.domain.resquest.ReqRegisterDTO;
import com.job.jobhunter.service.UserService;
import com.job.jobhunter.util.SecurityUtil;
import com.job.jobhunter.util.annotation.ApiMessage;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
                          UserService userService) {
        this.userService = userService;
        this.securityUtil = securityUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {

        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create Token
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        User user = this.userService.handleGetUserByUserName(loginDTO.getUsername());
        if (user != null) {
            ResLoginDTO.UserLogin userLogin = new UserLogin(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole());
            res.setUserLogin(userLogin);
        }

        String accessToken = this.securityUtil.createAccessToken(authentication.getName(), res);
        res.setAccessToken(accessToken);

        // create refresh token
        String refreshToken = this.securityUtil.createRefreshToken(authentication.getName(), res);

        // update refresh token to db
        this.userService.handleUpdateRefreshToken(refreshToken, loginDTO.getUsername());

        // set cookies
        ResponseCookie resCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 30) // 30 days
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        User currentUserDB = this.userService.handleGetUserByUserName(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();

        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
            userLogin.setRole(currentUserDB.getRole());
            userGetAccount.setUser(userLogin);
        }

        return ResponseEntity.ok().body(userGetAccount);
    }

    // ĐÃ FIX: Chuyển sang dùng @PostMapping cho chuẩn RESTful
    @PostMapping("/auth/refresh")
    @ApiMessage("refresh token")
    public ResponseEntity<ResLoginDTO> refreshToken(
            @CookieValue(value = "refreshToken", defaultValue = "") String refreshToken) {

        // check refresh token
        Jwt jwt = this.securityUtil.checkRefreshToken(refreshToken);
        String email = jwt.getSubject();

        // check user
        User user = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với email: " + email);
        }

        User currentUser = this.userService.handleGetUserByUserName(email);
        ResLoginDTO res = new ResLoginDTO();

        if (currentUser != null) {
            ResLoginDTO.UserLogin userLogin = new UserLogin(
                    currentUser.getId(),
                    currentUser.getEmail(),
                    currentUser.getName(),
                    currentUser.getRole());
            res.setUserLogin(userLogin);
        }

        String accessToken = this.securityUtil.createAccessToken(email, res);
        res.setAccessToken(accessToken);

        // create refresh token
        String newRefreshToken = this.securityUtil.createRefreshToken(email, res);

        // update refresh token to db
        this.userService.handleUpdateRefreshToken(newRefreshToken, email);

        // set cookies
        ResponseCookie resCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 30) // 30 days
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(res);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("logout account")
    public ResponseEntity<Void> logout(
            @CookieValue(value = "refreshToken", defaultValue = "") String refreshToken) {

        Jwt jwt = this.securityUtil.checkRefreshToken(refreshToken);
        String email = jwt.getSubject();
        User user = this.userService.handleGetUserByUserName(email);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với email: " + email);
        }

        this.userService.handleUpdateRefreshToken(null, email);

        // xóa cookie
        ResponseCookie resCookie = ResponseCookie.from("refreshToken", null)
                .httpOnly(true)
                .path("/")
                .maxAge(0) // 0 để trình duyệt xóa cookie ngay lập tức
                .build();

        // ĐÃ FIX: Bổ sung header SET_COOKIE để gửi lệnh xóa cookie về client
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(null);
    }

    // ĐÃ FIX: Sử dụng ReqRegisterDTO thay vì Entity User
    @PostMapping("/auth/register")
    @ApiMessage("register account")
    public ResponseEntity<UserCreateDTO> register(@Valid @RequestBody ReqRegisterDTO registerDTO) {
        // Map data từ DTO sang Entity
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setName(registerDTO.getName());

        UserCreateDTO userCreateDTO = this.userService.createNewUser(user);
        return ResponseEntity.ok().body(userCreateDTO);
    }
}