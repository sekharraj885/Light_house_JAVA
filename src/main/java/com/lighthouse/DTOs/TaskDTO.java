package com.lighthouse.DTOs;

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
public class TaskDTO {
    private UUID id;
    private String name;
    private String description;
    private String status;
    private UUID milestone_id;
    private Date startDate;
    private Date plannedEndDate;
    private Date actualEndDate;
    private String createdBy;
    private Date createdOn;
    private String lastUpdatedBy;
    private Date lastUpdatedOn;
}


