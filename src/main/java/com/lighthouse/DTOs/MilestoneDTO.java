package com.lighthouse.DTOs;

import com.lighthouse.enums.MilestoneStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MilestoneDTO {
    private UUID id;
    private String name;
    private String description;
    private MilestoneStatus status;
    private Date startDate;
    private Date plannedEndDate;
    private Date actualEndDate;
    private String createdBy;
    private Date createdOn;
    private String lastUpdatedBy;
    private Date lastUpdatedOn;
}
