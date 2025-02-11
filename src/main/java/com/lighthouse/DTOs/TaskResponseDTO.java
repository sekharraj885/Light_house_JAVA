package com.lighthouse.DTOs;

import com.lighthouse.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private TaskStatus status;
    private UUID milestoneId;
    private UUID parentTaskId;
    private List<UUID> subtaskIds;
    private Date startDate;
    private Date plannedEndDate;
    private Date actualEndDate;
    private String createdBy;
    private Date createdOn;
    private String lastUpdatedBy;
    private Date lastUpdatedOn;
}

