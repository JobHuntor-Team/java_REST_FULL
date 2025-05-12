package com.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import com.hoidanit.jobhunter.util.constent.GenderEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateUserDTO {
    private Long id;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;

}