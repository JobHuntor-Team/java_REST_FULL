package com.hoidanit.jobhunter.domain.response.resume;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Setter
@Getter
public class ResUpdateResumeDTO {
    private String updatedBy;
    private Instant updatedAt;
}
