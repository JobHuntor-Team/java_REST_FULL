package com.job.jobhunter.domain.response.file;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResUploadFileDTO {
    private String name;
    private Instant uploadAt;
}
