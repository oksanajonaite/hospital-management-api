# Hospital Management REST API

A Spring Boot REST API for hospital patient management with role-based access control using Spring Security.

## Features

- **Role-based access control** with three roles:
  - `RECEPTIONIST` -- registers new patients
  - `NURSE` -- records vital signs
  - `DOCTOR` -- records vitals and issues prescriptions
- **Patient management** -- registration, lookup
- **Vital signs tracking** -- heart rate, blood pressure, recorded by staff
- **Prescription management** -- medication name, dosage, issued by doctor
- **Event-driven alerts** -- automatic warning when heart rate exceeds 120 bpm (Spring Application Events)
- **H2 in-memory database** with web console for development

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Security (HTTP Basic + role-based authorization)
- Spring Data JPA / Hibernate
- H2 Database
- Lombok
- Maven

## Getting Started

```bash
git clone https://github.com/YOUR_USERNAME/hospital.git
cd hospital
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:hospital`).

## API Endpoints

| Method | Endpoint                        | Role(s)          | Description              |
|--------|---------------------------------|------------------|--------------------------|
| POST   | `/patients`                     | RECEPTIONIST     | Register a new patient   |
| GET    | `/patients/{id}`                | NURSE, DOCTOR    | Get patient details      |
| POST   | `/patients/{id}/vitals`         | NURSE, DOCTOR    | Record vital signs       |
| POST   | `/patients/{id}/prescriptions`  | DOCTOR           | Issue a prescription     |

## Demo Users

The app seeds four users on startup (password for all: `demo`):

| Username | Roles              |
|----------|---------------------|
| anna     | RECEPTIONIST        |
| brian    | NURSE               |
| clara    | DOCTOR              |
| david    | NURSE, DOCTOR       |

## Usage Examples

Register a patient (as receptionist):
```bash
curl -u anna:demo -X POST http://localhost:8080/patients \
  -H "Content-Type: application/json" \
  -d '{"fullName": "John Doe", "dateOfBirth": "1990-05-15"}'
```

Record vitals (as nurse):
```bash
curl -u brian:demo -X POST http://localhost:8080/patients/1/vitals \
  -H "Content-Type: application/json" \
  -d '{"heartRate": 130, "bloodPressure": "140/90"}'
```

Issue a prescription (as doctor):
```bash
curl -u clara:demo -X POST http://localhost:8080/patients/1/prescriptions \
  -H "Content-Type: application/json" \
  -d '{"medicationName": "Aspirin", "dosage": "100mg daily"}'
```

## Configuration

| Environment Variable | Default | Description            |
|---------------------|---------|------------------------|
| `DB_USERNAME`       | `sa`    | Database username       |
| `DB_PASSWORD`       | *(empty)* | Database password     |
| `APP_SEED_PASSWORD` | `demo`  | Password for demo users |
