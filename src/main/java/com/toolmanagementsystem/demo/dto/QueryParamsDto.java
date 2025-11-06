package com.toolmanagementsystem.demo.dto;


import com.toolmanagementsystem.demo.enums.SiteLocation;
import com.toolmanagementsystem.demo.enums.SiteType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class QueryParamsDto {



    private String siteLocation;
    private String siteType;
    private String facilityCode;
    private Boolean isActive;




}
//query lets you control what is being selected, sorted, grouped, or made distinct.
//You typically use query for sorting, distinct results, or custom joins.
//public static Specification<Facility> orderByLatestId() {
//    return (root, query, cb) -> {
//        query.orderBy(cb.desc(root.get("id")));  // Sort by id DESC
//        return cb.conjunction(); // return 'true' predicate (no filter)
//    };
//}

