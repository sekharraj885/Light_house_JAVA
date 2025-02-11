package com.lighthouse.utils.Query;

import com.lighthouse.models.Milestone;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MilestoneQuery {
    public static Specification<Milestone> filterMilestones(
            String name,
            String status,
            Date startDateFrom,
            Date startDateTo,
            Date plannedEndDateFrom,
            Date plannedEndDateTo,
            Boolean deleted
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (startDateFrom != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDateFrom));
            }
            if (startDateTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), startDateTo));
            }
            if (plannedEndDateFrom != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("plannedEndDate"), plannedEndDateFrom));
            }
            if (plannedEndDateTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("plannedEndDate"), plannedEndDateTo));
            }
            if (deleted != null) {
                predicates.add(criteriaBuilder.equal(root.get("deleted"), deleted));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
