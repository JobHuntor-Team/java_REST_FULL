package com.job.jobhunter.domain.response;

import java.time.Instant;

import com.job.jobhunter.util.constent.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private long id;
    private String email;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private Instant createdAt;
    private CompanyUser company;
    private UserCreateDTO.RoleUserDTO roleUserDTO;

    public ResUserDTO(long id, String email, String name, GenderEnum gender, String address, int age, Instant updatedAt, Instant createdAt, CompanyUser companyUser) {
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;
    }


}
