package com.toolmanagementsystem.demo.repository;

import com.toolmanagementsystem.demo.entity.Facility;
import com.toolmanagementsystem.demo.entity.Tool;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long>, JpaSpecificationExecutor<Tool> {
    List<Tool> findByFacilityId(Long facilityId);
}
