package com.example.hospital.event;

import org.springframework.context.ApplicationEvent;

public class HighHeartRateEvent extends ApplicationEvent {

    private final Long patientId;
    private final int heartRate;

    public HighHeartRateEvent(Object source, Long patientId, int heartRate) {
        super(source);
        this.patientId = patientId;
        this.heartRate = heartRate;
    }

    public Long getPatientId() {
        return patientId;
    }

    public int getHeartRate() {
        return heartRate;
    }
}
