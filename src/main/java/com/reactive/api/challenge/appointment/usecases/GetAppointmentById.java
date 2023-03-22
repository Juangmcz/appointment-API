package com.reactive.api.challenge.appointment.usecases;

import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import com.reactive.api.challenge.appointment.repository.IAppointmentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;
@Service
@AllArgsConstructor
public class GetAppointmentById implements Function<String, Mono<AppointmentDTO>> {

    private final IAppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Mono<AppointmentDTO> apply(String id) {
        return this.appointmentRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable("Not found")))
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .onErrorResume(Mono::error);
    }
}
