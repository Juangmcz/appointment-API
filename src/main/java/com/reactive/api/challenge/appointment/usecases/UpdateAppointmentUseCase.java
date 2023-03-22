package com.reactive.api.challenge.appointment.usecases;

import com.reactive.api.challenge.appointment.domain.collection.Appointment;
import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import com.reactive.api.challenge.appointment.repository.IAppointmentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Service
@AllArgsConstructor
public class UpdateAppointmentUseCase implements BiFunction<String, AppointmentDTO, Mono<AppointmentDTO>> {

    private final IAppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Mono<AppointmentDTO> apply(String id, AppointmentDTO appointmentDTO) {
        return appointmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new Throwable("Not found")))
                .flatMap(customer -> {
                    appointmentDTO.setId(customer.getId());
                    return appointmentRepository.save(modelMapper.map(appointmentDTO, Appointment.class));
                }).map(customer -> modelMapper.map(customer, AppointmentDTO.class))
                .onErrorResume(Mono::error);
    }
}
