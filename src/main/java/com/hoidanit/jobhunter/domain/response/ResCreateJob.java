package com.hoidanit.jobhunter.domain.response;

import java.time.Instant;
import java.util.List;

import com.hoidanit.jobhunter.util.constent.LevelEnum;
import lombok.AllArgsConstructor;
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
