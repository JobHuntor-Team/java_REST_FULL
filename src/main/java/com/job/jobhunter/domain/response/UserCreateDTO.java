package com.job.jobhunter.domain.response;

import java.time.Instant;

import com.job.jobhunter.util.constent.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {
    private Long id;
    private String email;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
    private UserCompany company;
    private RoleUserDTO roleUserDTO;

    @Getter
    @Setter
    public static class UserCompany {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUserDTO {
        private Long id;
        private String name;
        private boolean active;
    }

}
