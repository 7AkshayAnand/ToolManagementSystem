package com.toolmanagementsystem.demo.repository;

import com.toolmanagementsystem.demo.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository  extends JpaRepository<Facility,Long> {
    boolean existsByFacilityCode(String facilityCode);
}
