package com.reactive.api.challenge.appointment.usecases;

import com.reactive.api.challenge.appointment.domain.collection.Appointment;
import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import com.reactive.api.challenge.appointment.repository.IAppointmentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;
@Service
@AllArgsConstructor
public class SaveAppointmentUseCase implements Function<AppointmentDTO, Mono<AppointmentDTO>> {

    private final IAppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Mono<AppointmentDTO> apply(AppointmentDTO appointmentDTO) {
        return this.appointmentRepository.save(modelMapper.map(appointmentDTO, Appointment.class))
                .switchIfEmpty(Mono.empty())
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .onErrorResume(Mono::error);
    }
}
