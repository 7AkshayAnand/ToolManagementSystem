package com.toolmanagementsystem.demo.spefication;

import com.toolmanagementsystem.demo.entity.Facility;
import com.toolmanagementsystem.demo.enums.SiteLocation;
import com.toolmanagementsystem.demo.enums.SiteType;
import org.springframework.data.jpa.domain.Specification;

public class FacilitySpecification {

    // siteLocation filter
    public static Specification<Facility> hasSiteLocation(SiteLocation siteLocation) {
        return (root, query, cb) -> {
            if (siteLocation == null) return null;
            return cb.equal(cb.upper(root.get("siteLocation").as(String.class)), siteLocation.name().toUpperCase());
        };
    }


    // siteType filter
    public static Specification<Facility> hasSiteType(SiteType siteType) {
        return (root, query, cb) -> siteType == null ? null
                : cb.equal(root.get("siteType"), siteType);
    }
    // Filter by facility code (partial match, case-insensitive)
    public static Specification<Facility> hasFacilityCode(String facilityCode) {
        return (root, query, cb) ->
                facilityCode == null || facilityCode.isBlank() ? null
                        : cb.like(cb.lower(root.get("facilityCode")), "%" + facilityCode.toLowerCase() + "%");
    }

    // Filter by isActive
    public static Specification<Facility> isActive(Boolean isActive) {
        return (root, query, cb) ->
                isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }
}
