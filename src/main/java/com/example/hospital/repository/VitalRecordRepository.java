package com.example.hospital.repository;

import com.example.hospital.model.VitalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VitalRecordRepository extends JpaRepository<VitalRecord, Long> {
}
