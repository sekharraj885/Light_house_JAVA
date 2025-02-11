package com.lighthouse.services;

import com.lighthouse.DTOs.TaskRequestDTO;
import com.lighthouse.DTOs.TaskResponseDTO;
import com.lighthouse.enums.TaskStatus;
import com.lighthouse.mappers.TaskMapper;
import com.lighthouse.models.Milestone;
import com.lighthouse.models.Task;
import com.lighthouse.repositories.MilestoneRepository;
import com.lighthouse.repositories.TaskRepository;
import com.lighthouse.utils.Query.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    MilestoneRepository milestoneRepository;

    TaskStatus taskStatus;

    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskDTO) {
        Optional<Milestone> milestone = Optional.empty();
        if (taskDTO.getMilestoneId() != null) {
            milestone = milestoneRepository.findById(taskDTO.getMilestoneId());
        }
        List<UUID> subtasksIds = taskDTO.getSubtaskIds();
        Task newTask = TaskMapper.toEntity(taskDTO, milestone.orElse(null));
        Task savedTask = taskRepository.save(newTask);
        return TaskMapper.toResponseDTO(savedTask);
    }

    public Page<TaskResponseDTO> getAllTasks(
            String name,
            String status,
            UUID milestoneId,
            UUID assignedUserId,
            Date startDateFrom,
            Date startDateTo,
            Date plannedEndDateFrom,
            Date plannedEndDateTo,
            Boolean deleted,
            int page,
            int size    ){
        Specification<Task> spec = TaskQuery.filterTasks(
                name, status, milestoneId, assignedUserId, startDateFrom, startDateTo,
                plannedEndDateFrom, plannedEndDateTo, deleted
        );
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        Page<Task> allTasks = taskRepository.findAll(spec, pageable);
      return allTasks.map(TaskMapper::toResponseDTO);
    }

    public TaskResponseDTO getTaskById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return TaskMapper.toResponseDTO(task);
    }

    @Transactional
    public TaskResponseDTO updateTask(TaskRequestDTO taskDTO) {

        Task existingTask = taskRepository.findById(taskDTO.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (taskDTO.getName() != null) {
            existingTask.setName(taskDTO.getName());
        }
        if (taskDTO.getDescription() != null) {
            existingTask.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getStatus() != null) {
            existingTask.setStatus(taskDTO.getStatus());
        }
        if (taskDTO.getStartDate() != null) {
            existingTask.setStartDate(taskDTO.getStartDate());
        }
        if (taskDTO.getPlannedEndDate() != null) {
            existingTask.setPlannedEndDate(taskDTO.getPlannedEndDate());
        }
        if (taskDTO.getActualEndDate() != null) {
            existingTask.setActualEndDate(taskDTO.getActualEndDate());
        }
        if (taskDTO.getDeleted() != null) {
            existingTask.setDeleted(taskDTO.getDeleted());
        }

        if (taskDTO.getMilestoneId() != null) {
            Milestone milestone = milestoneRepository.findById(taskDTO.getMilestoneId())
                    .orElseThrow(() -> new RuntimeException("Milestone not found"));
            existingTask.setMilestone(milestone);
        }

        if (taskDTO.getParentTaskId() != null) {
            existingTask.setParentTaskId(taskDTO.getParentTaskId());
        }

        if (taskDTO.getSubtaskIds() != null) {
            existingTask.setSubtasks(taskDTO.getSubtaskIds());
        }

        Task updatedTask = taskRepository.save(existingTask);

        return TaskMapper.toResponseDTO(updatedTask);
    }



}
