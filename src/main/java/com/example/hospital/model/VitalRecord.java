package com.example.hospital.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vital_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VitalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private int heartRate;
    private String bloodPressure;

    @Column(nullable = false)
    private String recordedBy;

    @Column(nullable = false)
    private LocalDateTime recordedAt;
}
