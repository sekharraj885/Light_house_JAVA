package com.lighthouse.controllers;

import com.lighthouse.DTOs.TaskDTO;
import com.lighthouse.DTOs.TaskRequestDTO;
import com.lighthouse.DTOs.TaskResponseDTO;
import com.lighthouse.models.Task;

import com.lighthouse.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create-task")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskDTO) {
        TaskResponseDTO createdTask = taskService.createTask(taskDTO);
        return new ResponseEntity<>(createdTask,HttpStatus.CREATED);
    }

    @GetMapping("/get-all-tasks")
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID milestoneId,
            @RequestParam(required = false) UUID assignedUserId,
            @RequestParam(required = false) Date startDateFrom,
            @RequestParam(required = false) Date startDateTo,
            @RequestParam(required = false) Date plannedEndDateFrom,
            @RequestParam(required = false) Date plannedEndDateTo,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
    ){
        Page<TaskResponseDTO> tasks = taskService.getAllTasks( name, status, milestoneId, assignedUserId, startDateFrom, startDateTo,
                plannedEndDateFrom, plannedEndDateTo, deleted, page, size);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/get-tasks/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable UUID id) {
        try {
            TaskResponseDTO task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/update-task")
    public ResponseEntity<TaskResponseDTO>updateTask(@RequestBody TaskRequestDTO task){
        TaskResponseDTO updatedTask = taskService.updateTask(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.CREATED);

    }
}
