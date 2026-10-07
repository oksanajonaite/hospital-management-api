package com.example.hospital.dto;

import java.time.LocalDate;

public record PatientRequest(String fullName, LocalDate dateOfBirth) {
}
