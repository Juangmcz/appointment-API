package com.reactive.api.challenge.appointment.domain.customer;

import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import lombok.Data;

import java.util.List;
@Data
public class CustomerDTO {

    private String id;
    private String name;
    private String lastName;
    private String prefix;
    private String cell;
    private List<AppointmentDTO> appointmentsDTO;
}
