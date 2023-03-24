package com.reactive.api.challenge.appointment.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import com.reactive.api.challenge.appointment.publisher.AppointmentPublisher;
import com.reactive.api.challenge.appointment.repository.IAppointmentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ScheduleAppointmentUseCase {

    private final IAppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;
    private final AppointmentPublisher appointmentPublisher;

    public Mono<AppointmentDTO> schedule(String appointmentId, String customerId){

        return this.appointmentRepository
                .findById(appointmentId)
                .switchIfEmpty(Mono.error(new Throwable("Appointment does not exist")))
                .flatMap(appointment -> {
                    if (!appointment.isScheduled()) {
                        appointment.setScheduled(true);
                        return this.appointmentRepository.save(appointment);
                    }
                    return Mono.error(new Throwable("The Appointment is already scheduled"));
                })
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .doOnSuccess(appointmentDTO -> {
                    try {
                        appointmentPublisher.publish(appointmentDTO, customerId);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });

    }
}
