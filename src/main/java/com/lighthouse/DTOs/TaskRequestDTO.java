package com.lighthouse.DTOs;

import com.lighthouse.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
        private UUID id;
        private String name;
        private String description;
        private TaskStatus status;
        private UUID milestoneId;
        private UUID parentTaskId;
        private List<UUID> subtaskIds; // Contains only summary details
        private Date startDate;
        private Date plannedEndDate;
        private Date actualEndDate;
        private Boolean deleted;
    }


