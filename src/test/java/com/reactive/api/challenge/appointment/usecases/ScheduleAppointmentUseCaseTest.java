package com.reactive.api.challenge.appointment.usecases;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactive.api.challenge.appointment.domain.collection.Appointment;
import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import com.reactive.api.challenge.appointment.publisher.AppointmentPublisher;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ScheduleAppointmentUseCaseTest {

    @Mock
    IAppointmentRepository mockedRepository;
    ModelMapper modelMapper;
    ScheduleAppointmentUseCase scheduleAppointmentUseCase;
    AppointmentPublisher appointmentPublisher;
    @Mock
    RabbitTemplate rabbitTemplate;

    @BeforeEach
    void init() {
        modelMapper = new ModelMapper();
        appointmentPublisher = new AppointmentPublisher(rabbitTemplate, new ObjectMapper());
        scheduleAppointmentUseCase = new ScheduleAppointmentUseCase(mockedRepository, modelMapper, appointmentPublisher);
    }

    @Test
    @DisplayName("scheduleAppointment_Success")
    void scheduleAppointment(){

        var appointment = new Appointment("03/22/2023", "Ryan Watson");

        Mockito.when(mockedRepository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(appointment));
        Mockito.when(mockedRepository.save(ArgumentMatchers.any(Appointment.class))).thenReturn(Mono.just(modelMapper.map(appointment, Appointment.class)));

        var response = scheduleAppointmentUseCase.schedule(appointment.getId(), "customerId");

        StepVerifier.create(response)
                .expectNextMatches(AppointmentDTO::isScheduled)
                .verifyComplete();
    }

    @Test
    @DisplayName("scheduleAppointment_NonSuccess")
    void scheduleInvalidAppointment(){

        Mockito.when(mockedRepository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        var response = scheduleAppointmentUseCase.schedule("invalidId","invalidId");

        StepVerifier.create(response)
                .expectError(Throwable.class);

        Mockito.verify(mockedRepository).findById(ArgumentMatchers.anyString());
    }
}