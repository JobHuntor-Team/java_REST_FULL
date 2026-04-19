package com.job.jobhunter.domain.response;

import com.job.jobhunter.util.constent.GenderEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;

@Getter
@Setter
public class UserCreateDTO {
    private Long id;
    @NotBlank
    private String email;

    @Length(min = 2, message = "name must be at least 6 characters", max = 60)
    @NotBlank
    private String name;
    @NotBlank
    private GenderEnum gender;
    @NotBlank
    private String address;
    @NotBlank
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
