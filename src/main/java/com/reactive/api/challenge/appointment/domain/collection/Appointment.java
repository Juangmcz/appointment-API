package com.reactive.api.challenge.appointment.domain.collection;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "appointments")
public class Appointment {

    private String id;
    @NotNull(message = "The date cannot be null")
    private String date;
    @NotNull(message = "The barber cannot be null")
    private String barberName;
    private boolean isScheduled = false;

    public Appointment(String date, String barberName) {
        this.id = UUID.randomUUID().toString().substring(0, 10);
        this.date = date;
        this.barberName = barberName;
        this.isScheduled = false;
    }
}
