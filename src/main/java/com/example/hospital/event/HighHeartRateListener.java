package com.example.hospital.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HighHeartRateListener {

    @EventListener
    public void onHighHeartRate(HighHeartRateEvent event) {
        System.out.printf("WARN: Patient %d has critical heart rate (%d bpm) — attending doctor notified.%n",
                event.getPatientId(), event.getHeartRate());
    }
}
