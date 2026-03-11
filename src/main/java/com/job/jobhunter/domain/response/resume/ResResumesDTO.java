package com.job.jobhunter.domain.response.resume;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResResumesDTO {

    private Long id;
    private String email;
    private String url;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private UserResumeDTO user;
    private JobResumeDTO job;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResumeDTO {
        private Long id;
        private String name;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobResumeDTO {
        private Long id;
        private String name;
    }

}