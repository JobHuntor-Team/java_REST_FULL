package com.job.jobhunter.domain.response;

import java.time.Instant;
import java.util.List;

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

    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private List<String> skillIds;

}
