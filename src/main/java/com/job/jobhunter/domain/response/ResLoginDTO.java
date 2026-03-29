package com.job.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.job.jobhunter.domain.Role;

// ĐÃ XÓA TOÀN BỘ LOMBOK (@Getter, @Setter...) ĐỂ CODE TỰ CHẠY BẰNG TAY
public class ResLoginDTO {

    @JsonProperty("access_token")
    private String accessToken;
    private UserLogin userLogin;

    // --- Tự viết Getter/Setter cho ResLoginDTO ---
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    // ==========================================
    public static class UserLogin {
        private long id;
        private String email;
        private String name;
        private Role role;

        // Constructor rỗng
        public UserLogin() {}

        // Constructor 4 tham số
        public UserLogin(long id, String email, String name, Role role) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.role = role;
        }

        // --- Tự viết Getter/Setter cho UserLogin ---
        public long getId() { return id; }
        public void setId(long id) { this.id = id; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }
    }

    // ==========================================
    public static class UserGetAccount {
        private UserLogin user;

        public UserGetAccount() {}

        public UserLogin getUser() { return user; }
        public void setUser(UserLogin user) { this.user = user; }
    }

    // ==========================================
    public static class UserInsideToken {
        private Long id;
        private String email;
        private String name;

        public UserInsideToken() {}

        public UserInsideToken(Long id, String email, String name) {
            this.id = id;
            this.email = email;
            this.name = name;
        }

        // --- Tự viết Getter/Setter cho UserInsideToken ---
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}