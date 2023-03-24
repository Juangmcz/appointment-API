package com.reactive.api.challenge.appointment.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppointmentDTO {

    private String id;
    @NotNull(message = "The date cannot be null")
    private String date;
    @NotNull(message = "The barber cannot be null")
    private String barberName;
    private boolean isScheduled;

    public AppointmentDTO(String date, String barberName) {
        this.date = date;
        this.barberName = barberName;
    }
}
