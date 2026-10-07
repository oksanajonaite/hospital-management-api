package com.example.hospital.controller;

import com.example.hospital.dto.PatientRequest;
import com.example.hospital.dto.PrescriptionRequest;
import com.example.hospital.dto.VitalRequest;
import com.example.hospital.model.Patient;
import com.example.hospital.model.Prescription;
import com.example.hospital.model.VitalRecord;
import com.example.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // POST /patients — RECEPTIONIST only
    @PostMapping
    public ResponseEntity<Patient> registerPatient(@RequestBody PatientRequest req) {
        Patient saved = patientService.register(req);
        return ResponseEntity.created(URI.create("/patients/" + saved.getId())).body(saved);
    }

    // GET /patients/{id} — NURSE, DOCTOR
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    // POST /patients/{id}/vitals — NURSE, DOCTOR
    @PostMapping("/{id}/vitals")
    public ResponseEntity<VitalRecord> addVitals(
            @PathVariable Long id,
            @RequestBody VitalRequest req,
            Authentication authentication) {
        return ResponseEntity.ok(patientService.addVitals(id, req, authentication.getName()));
    }

    // POST /patients/{id}/prescriptions — DOCTOR only
    @PostMapping("/{id}/prescriptions")
    public ResponseEntity<Prescription> addPrescription(
            @PathVariable Long id,
            @RequestBody PrescriptionRequest req,
            Authentication authentication) {
        return ResponseEntity.ok(patientService.addPrescription(id, req, authentication.getName()));
    }
}
