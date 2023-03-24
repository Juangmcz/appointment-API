package com.reactive.api.challenge.appointment.publisher;

import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppointmentEvent {

    private String customerId;
    private AppointmentDTO appointmentScheduled;
    private String eventType;
}
