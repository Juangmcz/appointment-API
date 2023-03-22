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
class DeleteAppointmentUseCaseTest {

    @Mock
    IAppointmentRepository mockedRepository;
    ModelMapper modelMapper;
    DeleteAppointmentUseCase deleteAppointmentUseCase;

    @BeforeEach
    void init(){
        modelMapper = new ModelMapper();
        deleteAppointmentUseCase = new DeleteAppointmentUseCase(mockedRepository);
    }

    @Test
    @DisplayName("deleteAppointment_Success")
    void deleteAppointment() {

        var appointment = new Appointment("03/22/2023", "William");
        appointment.setId("appointmentId");

        Mockito.when(mockedRepository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(appointment));

        Mockito.when(mockedRepository.deleteById(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        var response = deleteAppointmentUseCase.apply("appointmentId");

        StepVerifier.create(response)
                .expectNextCount(0)
                .verifyComplete();

        Mockito.verify(mockedRepository).deleteById(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("deleteInvalidAppointment_NonSuccess")
    void deleteInvalidAppointment(){

        Mockito.when(mockedRepository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        var response = deleteAppointmentUseCase.apply("");

        StepVerifier.create(response)
                .expectError(Throwable.class);

        Mockito.verify(mockedRepository).findById("");

    }

}