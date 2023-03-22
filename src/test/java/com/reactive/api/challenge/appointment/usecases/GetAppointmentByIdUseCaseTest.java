package com.reactive.api.challenge.appointment.usecases;

import com.reactive.api.challenge.appointment.domain.collection.Appointment;
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
class GetAppointmentByIdUseCaseTest {

    @Mock
    IAppointmentRepository mockedRepository;
    ModelMapper modelMapper;
    GetAppointmentByIdUseCase getAppointmentByIdUseCase;

    @BeforeEach
    void init(){
        modelMapper = new ModelMapper();
        getAppointmentByIdUseCase = new GetAppointmentByIdUseCase(mockedRepository, modelMapper);
    }

    @Test
    @DisplayName("getAppointmentById_Success")
    void getAppointmentById() {

        var appointment = new Appointment("03/22/2023", "William");
        appointment.setId("appointmentId");

        Mockito.when(mockedRepository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(appointment));

        var response = getAppointmentByIdUseCase.apply("appointmentId");

        StepVerifier.create(response)
                .expectNextCount(1)
                .verifyComplete();

        Mockito.verify(mockedRepository).findById("appointmentId");
    }

    @Test
    @DisplayName("getAppointmentByWrongId_NonSuccess")
    void getAppointmentByWrongId() {

        Mockito.when(mockedRepository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        var response = getAppointmentByIdUseCase.apply("appointmentId");

        StepVerifier.create(response)
                .expectError(Throwable.class);

        Mockito.verify(mockedRepository).findById("appointmentId");

    }

}