package com.lighthouse.mappers;

import com.lighthouse.DTOs.TaskRequestDTO;
import com.lighthouse.DTOs.TaskResponseDTO;
import com.lighthouse.models.Milestone;
import com.lighthouse.models.Task;
import org.springframework.stereotype.Component;


@Component
public class TaskMapper {

    // Convert Entity to Response DTO
    public static TaskResponseDTO toResponseDTO(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskResponseDTO(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                task.getMilestone() != null ? task.getMilestone().getId() : null,
                task.getParentTaskId() != null ? task.getParentTaskId() : null,
                task.getSubtasks() != null ? task.getSubtasks() : null,
                task.getStartDate(),
                task.getPlannedEndDate(),
                task.getActualEndDate(),
                task.getCreatedBy(),
                task.getCreatedOn(),
                task.getLastUpdatedBy(),
                task.getLastUpdatedOn()
        );
    }


    // Convert DTO to Entity
    public static Task toEntity(TaskRequestDTO dto, Milestone milestone) {
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setMilestone(milestone);
        task.setParentTaskId(dto.getParentTaskId());
        task.setSubtasks(dto.getSubtaskIds());
        task.setStartDate(dto.getStartDate());
        task.setPlannedEndDate(dto.getPlannedEndDate());
        task.setActualEndDate(dto.getActualEndDate());
        task.setDeleted(dto.getDeleted());
        return task;
    }
}
