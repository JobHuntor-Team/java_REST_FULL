package com.job.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.job.jobhunter.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class ResLoginDTO {

    @JsonProperty("access_token")
    private String accessToken;
    private UserLogin userLogin;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class UserLogin {
        public UserLogin(long id, String email, String name, Role role) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.role = role;
        }

        private long id;
        private String email;
        private String name;
        private Role role;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserGetAccount {
        private UserLogin user;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInsideToken {
        private Long id;
        private String email;
        private String name;
    }

}
