package com.reactive.api.challenge.appointment.usecases;

import com.reactive.api.challenge.appointment.domain.collection.Appointment;
import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import com.reactive.api.challenge.appointment.repository.IAppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UpdateAppointmentUseCaseTest {

    @Mock
    IAppointmentRepository mockedRepository;
    ModelMapper modelMapper;
    UpdateAppointmentUseCase updateAppointmentUseCase;

    @BeforeEach
    void init() {
        modelMapper = new ModelMapper();
        updateAppointmentUseCase = new UpdateAppointmentUseCase(mockedRepository, modelMapper);
    }

    @Test
    @DisplayName("updateAppointment_Success")
    void updateAppointment() {

        var appointment = new Appointment("03/22/2023", "William");
        appointment.setId("appointmentId");

        Mockito.when(mockedRepository.findById("appointmentId")).thenReturn(Mono.just(appointment));

        Mockito.when(mockedRepository.save(ArgumentMatchers.any(Appointment.class))).thenReturn(Mono.just(appointment));

        var response = updateAppointmentUseCase.apply("appointmentId",
                modelMapper.map(appointment, AppointmentDTO.class)
        );

        StepVerifier.create(response)
                .expectNext(modelMapper.map(appointment, AppointmentDTO.class))
                .expectNextCount(0)
                .verifyComplete();

        Mockito.verify(mockedRepository).findById(ArgumentMatchers.anyString());
        Mockito.verify(mockedRepository).save(ArgumentMatchers.any(Appointment.class));
    }

    @Test
    @DisplayName("updateAppointment_NonSuccess")
    void updateInvalidAppointment() {

        var appointment = new Appointment("03/22/2023", "William");
        appointment.setId("appointmentId");

        Mockito.when(mockedRepository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        var response = updateAppointmentUseCase.apply("",modelMapper.map(appointment, AppointmentDTO.class));

        StepVerifier.create(response)
                .expectError(Throwable.class);

        Mockito.verify(mockedRepository).findById(ArgumentMatchers.anyString());

    }
}