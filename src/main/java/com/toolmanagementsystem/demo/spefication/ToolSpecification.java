package com.toolmanagementsystem.demo.spefication;

import com.toolmanagementsystem.demo.entity.Tool;
import com.toolmanagementsystem.demo.enums.ToolStatus;
import com.toolmanagementsystem.demo.enums.ToolType;
import org.springframework.data.jpa.domain.Specification;

public class ToolSpecification {



    public static Specification<Tool> hasToolType(ToolType toolType) {
        return (root, query, cb) ->
                toolType == null ? null :
                        cb.equal(root.get("toolType"), toolType);
    }

    public static Specification<Tool> hasStatus(ToolStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    public static Specification<Tool> hasToolName(String toolName) {
        return (root, query, cb) -> {
            if (toolName == null || toolName.isBlank()) {
                return null;
            }

            String value = toolName.trim().toLowerCase();

            // Case 1: Wildcard search if contains "*"
            if (value.contains("*")) {
                String pattern = value.replace("*", "%");
                return cb.like(cb.lower(root.get("toolName")), pattern);
            }

            // Case 2: Exact match (case-insensitive)
            return cb.equal(cb.lower(root.get("toolName")), value.toLowerCase());
        };
    }


    public static Specification<Tool> hasFacility(Long facilityId) {
        return (root, query, cb) ->
                facilityId == null ? null :
                        cb.equal(root.get("facility").get("id"), facilityId);
    }



    public static Specification<Tool> createdFrom(java.time.LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null :
                        cb.greaterThanOrEqualTo(root.get("createdDate"), from);
    }

    public static Specification<Tool> createdTo(java.time.LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null :
                        cb.lessThanOrEqualTo(root.get("createdDate"), to);
    }

    public static Specification<Tool> updatedFrom(java.time.LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null :
                        cb.greaterThanOrEqualTo(root.get("updatedDate"), from);
    }

    public static Specification<Tool> updatedTo(java.time.LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null :
                        cb.lessThanOrEqualTo(root.get("updatedDate"), to);
    }

}
