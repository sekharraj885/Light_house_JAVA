package com.lighthouse.services;

import com.lighthouse.DTOs.MilestoneDTO;
import com.lighthouse.exceptions.DuplicateResourceException;
import com.lighthouse.exceptions.ResourceNotFoundException;
import com.lighthouse.models.Milestone;
import com.lighthouse.repositories.MilestoneRepository;
import com.lighthouse.utils.Query.MilestoneQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MilestoneService {

    @Autowired
    private MilestoneRepository milestoneRepository;

    public MilestoneDTO createMilestone(Milestone milestone){
       try{
           Milestone newMilestone = milestoneRepository.save(milestone);
           return new MilestoneDTO(
                   newMilestone.getId(),
                   newMilestone.getName(),
                   newMilestone.getDescription(),
                   newMilestone.getStatus(),
                   newMilestone.getStartDate(),
                   newMilestone.getPlannedEndDate(),
                   newMilestone.getActualEndDate(),
                   newMilestone.getCreatedBy(),
                   newMilestone.getCreatedOn(),
                   newMilestone.getLastUpdatedBy(),
                   newMilestone.getLastUpdatedOn()
           );
       }catch(DataIntegrityViolationException ex){
           throw new DuplicateResourceException("Milestone with this name already exists");
       }
    }

    public Page<MilestoneDTO> getAllMilestone(
            String name,
            String status,
            Date startDateFrom,
            Date startDateTo,
            Date plannedEndDateFrom,
            Date plannedEndDateTo,
            Boolean deleted,
            int page,
            int size
    ){
        Specification<Milestone> spec = MilestoneQuery.filterMilestones(
                name, status, startDateFrom, startDateTo, plannedEndDateFrom, plannedEndDateTo, deleted
        );
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdOn").descending());
        Page<Milestone> milestonePage = milestoneRepository.findAll(spec, pageable);
        return milestonePage.map(milestone->
            new MilestoneDTO(
                    milestone.getId(),
                    milestone.getName(),
                    milestone.getDescription(),
                    milestone.getStatus(),
                    milestone.getStartDate(),
                    milestone.getPlannedEndDate(),
                    milestone.getActualEndDate(),
                    milestone.getCreatedBy(),
                    milestone.getCreatedOn(),
                    milestone.getLastUpdatedBy(),
                    milestone.getLastUpdatedOn()
            ));
    }

    public MilestoneDTO getMilestoneById(UUID id) {
        return milestoneRepository.findById(id)
                .map(milestone -> new MilestoneDTO(
                        milestone.getId(),
                        milestone.getName(),
                        milestone.getDescription(),
                        milestone.getStatus(),
                        milestone.getStartDate(),
                        milestone.getPlannedEndDate(),
                        milestone.getActualEndDate(),
                        milestone.getCreatedBy(),
                        milestone.getCreatedOn(),
                        milestone.getLastUpdatedBy(),
                        milestone.getLastUpdatedOn()
                ))
                .orElseThrow(() -> new ResourceNotFoundException("Milestone with ID " + id + " not found"));
    }


    @Transactional
    public Optional<MilestoneDTO> updateMilestone(UUID id, Milestone milestoneUpdates) {
        return milestoneRepository.findById(id).map(existingMilestone -> {
            // Update only provided fields
            if (milestoneUpdates.getName() != null) {
                existingMilestone.setName(milestoneUpdates.getName());
            }
            if (milestoneUpdates.getDescription() != null) {
                existingMilestone.setDescription(milestoneUpdates.getDescription());
            }
            if (milestoneUpdates.getStatus() != null) {
                existingMilestone.setStatus(milestoneUpdates.getStatus());
            }
            if (milestoneUpdates.getStartDate() != null) {
                existingMilestone.setStartDate(milestoneUpdates.getStartDate());
            }
            if (milestoneUpdates.getPlannedEndDate() != null) {
                existingMilestone.setPlannedEndDate(milestoneUpdates.getPlannedEndDate());
            }
            if (milestoneUpdates.getActualEndDate() != null) {
                existingMilestone.setActualEndDate(milestoneUpdates.getActualEndDate());
            }
            if (milestoneUpdates.getDeleted()!=null) {
                existingMilestone.setDeleted(milestoneUpdates.getDeleted());
            }

            Milestone newMilestone = milestoneRepository.save(existingMilestone);
            return new MilestoneDTO(
                    newMilestone.getId(),
                    newMilestone.getName(),
                    newMilestone.getDescription(),
                    newMilestone.getStatus(),
                    newMilestone.getStartDate(),
                    newMilestone.getPlannedEndDate(),
                    newMilestone.getActualEndDate(),
                    newMilestone.getCreatedBy(),
                    newMilestone.getCreatedOn(),
                    newMilestone.getLastUpdatedBy(),
                    newMilestone.getLastUpdatedOn()
            );
        });
    }

    @Transactional
    public void deleteMilestone(UUID id) {
        milestoneRepository.findById(id).ifPresent(milestone -> {
            milestone.setDeleted(true);
            milestoneRepository.save(milestone);
        });
    }

}
