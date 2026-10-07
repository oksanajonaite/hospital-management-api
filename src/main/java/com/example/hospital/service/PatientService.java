package com.example.hospital.service;

import com.example.hospital.dto.PatientRequest;
import com.example.hospital.dto.PrescriptionRequest;
import com.example.hospital.dto.VitalRequest;
import com.example.hospital.event.HighHeartRateEvent;
import com.example.hospital.model.Patient;
import com.example.hospital.model.Prescription;
import com.example.hospital.model.VitalRecord;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.repository.PrescriptionRepository;
import com.example.hospital.repository.VitalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final VitalRecordRepository vitalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Patient register(PatientRequest req) {
        Patient patient = new Patient(null, req.fullName(), req.dateOfBirth(), LocalDateTime.now());
        return patientRepository.save(patient);
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
    }

    public VitalRecord addVitals(Long patientId, VitalRequest req, String recordedBy) {
        Patient patient = findById(patientId);

        VitalRecord record = new VitalRecord(
                null, patient, req.heartRate(), req.bloodPressure(), recordedBy, LocalDateTime.now()
        );

        if (req.heartRate() > 120) {
            eventPublisher.publishEvent(new HighHeartRateEvent(this, patient.getId(), req.heartRate()));
        }

        return vitalRecordRepository.save(record);
    }

    public Prescription addPrescription(Long patientId, PrescriptionRequest req, String issuedBy) {
        Patient patient = findById(patientId);

        Prescription prescription = new Prescription(
                null, patient, req.medicationName(), req.dosage(), issuedBy, LocalDateTime.now()
        );

        return prescriptionRepository.save(prescription);
    }
}
