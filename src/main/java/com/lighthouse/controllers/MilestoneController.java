package com.lighthouse.controllers;

import com.lighthouse.DTOs.ErrorResponseDTO;
import com.lighthouse.DTOs.MilestoneDTO;
import com.lighthouse.exceptions.ResourceNotFoundException;
import com.lighthouse.models.Milestone;
import com.lighthouse.services.MilestoneService;
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
@RequestMapping("/api/milestones")
public class MilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @PostMapping("/create-milestone")
    public ResponseEntity<MilestoneDTO> createMilestone(@RequestBody Milestone milestone){
        MilestoneDTO savedMilestone =  milestoneService.createMilestone(milestone);
        return new ResponseEntity<>(savedMilestone, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-milestones")
    public ResponseEntity<Page<MilestoneDTO>> getAllMilestone(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Date startDateFrom,
            @RequestParam(required = false) Date startDateTo,
            @RequestParam(required = false) Date plannedEndDateFrom,
            @RequestParam(required = false) Date plannedEndDateTo,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
    ){
        Page<MilestoneDTO> milestones = milestoneService.getAllMilestone(name, status, startDateFrom, startDateTo, plannedEndDateFrom, plannedEndDateTo, deleted, page, size);
        return  new ResponseEntity<>(milestones,HttpStatus.OK);
    }

    @GetMapping("/get-milestone/{id}")
    public ResponseEntity<?> getMilestoneById(@PathVariable UUID id) {
        try {
            MilestoneDTO milestone = milestoneService.getMilestoneById(id);
            return ResponseEntity.ok(milestone);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
        }
    }

    @PatchMapping("/update-milestone/{id}")
    public ResponseEntity<Optional<MilestoneDTO>> updateMilestone(  @PathVariable UUID id,@RequestBody Milestone updatedMilestone) {
        Optional<MilestoneDTO> milestone = milestoneService.updateMilestone(id,updatedMilestone);
        return milestone != null ?
                new ResponseEntity<>(milestone, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
