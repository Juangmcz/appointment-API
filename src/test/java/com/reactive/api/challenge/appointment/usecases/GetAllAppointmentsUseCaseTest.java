package com.reactive.api.challenge.appointment.usecases;

import com.reactive.api.challenge.appointment.domain.collection.Appointment;
import com.reactive.api.challenge.appointment.repository.IAppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GetAllAppointmentsUseCaseTest {

    @Mock
    IAppointmentRepository mockedRepository;
    ModelMapper modelMapper;
    GetAllAppointmentsUseCase getAllAppointmentsUseCase;

    @BeforeEach
    void init(){
        modelMapper = new ModelMapper();
        getAllAppointmentsUseCase = new GetAllAppointmentsUseCase(mockedRepository, modelMapper);
    }

    @Test
    @DisplayName("getAllAppointments_Success")
    void getAllAppointments() {

        var appointment1 = new Appointment("03/22/2023", "William");
        var appointment2 = new Appointment("03/23/2023", "Robert");

        var fluxAppointments = Flux.just(appointment1, appointment2);

        Mockito.when(mockedRepository.findAll()).thenReturn(fluxAppointments);

        var response = getAllAppointmentsUseCase.get();

        StepVerifier.create(response)
                .expectNextCount(2)
                .verifyComplete();

        Mockito.verify(mockedRepository).findAll();

    }
}