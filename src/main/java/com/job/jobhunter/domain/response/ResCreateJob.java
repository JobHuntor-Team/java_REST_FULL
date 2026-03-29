package com.job.jobhunter.domain.response;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat; // Thêm import này
import com.job.jobhunter.util.constent.LevelEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateJob {

    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant endDate;

    // Bổ sung thêm trường createdAt ở đây
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant createdAt;

    private boolean active;
    private List<String> skillIds;

}