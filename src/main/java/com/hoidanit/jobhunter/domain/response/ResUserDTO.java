package com.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import com.hoidanit.jobhunter.domain.Company;
import com.hoidanit.jobhunter.domain.response.UserCreateDTO.UserCompany;
import com.hoidanit.jobhunter.util.constent.GenderEnum;

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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
