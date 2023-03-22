package com.reactive.api.challenge.appointment.usecases;

import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import com.reactive.api.challenge.appointment.repository.IAppointmentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class GetAllAppointmentsUseCase implements Supplier<Flux<AppointmentDTO>> {

    private final IAppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Flux<AppointmentDTO> get() {
        return this.appointmentRepository
                .findAll()
                .switchIfEmpty(Flux.empty())
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class));
    }
}
