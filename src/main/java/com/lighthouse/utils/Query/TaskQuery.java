package com.lighthouse.utils.Query;
import com.lighthouse.models.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
public class TaskQuery {





        public static Specification<Task> filterTasks(
                String name,
                String status,
                UUID milestoneId,
                UUID assignedUserId,
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
                if (milestoneId != null) {
                    predicates.add(criteriaBuilder.equal(root.get("milestone").get("id"), milestoneId));
                }
                if (assignedUserId != null) {
                    predicates.add(criteriaBuilder.equal(root.get("assignedUser").get("id"), assignedUserId));
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

