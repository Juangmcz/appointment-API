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
class SaveAppointmentUseCaseTest {

    @Mock
    IAppointmentRepository mockedRepository;
    ModelMapper modelMapper;
    SaveAppointmentUseCase saveAppointmentUseCase;

    @BeforeEach
    void init(){
        modelMapper = new ModelMapper();
        saveAppointmentUseCase = new SaveAppointmentUseCase(mockedRepository, modelMapper);
    }

    @Test
    @DisplayName("saveAppointment_Success")
    void saveAppointment(){

        var appointment = new Appointment("03/22/2023", "William");

        Mockito.when(mockedRepository.save(ArgumentMatchers.any(Appointment.class))).thenReturn(Mono.just(appointment));

        var response = saveAppointmentUseCase.apply(modelMapper.map(appointment, AppointmentDTO.class));

        StepVerifier.create(response)
                .expectNextCount(1)
                .verifyComplete();

        Mockito.verify(mockedRepository).save(ArgumentMatchers.any(Appointment.class));
    }

    @Test
    @DisplayName("saveAppointment_NonSuccess")
    void saveAppointmentFailed(){

        var appointment = new Appointment("03/22/2023", "William");

        Mockito.when(mockedRepository.save(ArgumentMatchers.any(Appointment.class))).thenReturn(Mono.empty());

        var response = saveAppointmentUseCase.apply(modelMapper.map(appointment, AppointmentDTO.class));

        StepVerifier.create(response)
                .expectError(Throwable.class);

        Mockito.verify(mockedRepository).save(ArgumentMatchers.any(Appointment.class));

    }
}