package com.lighthouse.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lighthouse.enums.MilestoneStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "milestone")
@EntityListeners(AuditingEntityListener.class)
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    @NotBlank(message = "Milestone name is required")
    @Size(min = 3, max = 100, message = "Milestone name must be between 3 and 100 characters")
    private String name;
    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private MilestoneStatus status;

    @FutureOrPresent(message = "Start date cannot be in the future")
    private Date startDate;

    @FutureOrPresent(message = "Start date cannot be in the future")
    private Date plannedEndDate;

    @PastOrPresent(message = "Actual end date cannot be in the future")
    private Date actualEndDate;

    private Boolean deleted =false;

    @JsonIgnore
    @OneToMany(mappedBy = "milestone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private Date createdOn;

    @LastModifiedBy
    private String lastUpdatedBy;

    @LastModifiedDate
    private Date lastUpdatedOn;

}
