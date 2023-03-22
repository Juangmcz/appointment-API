package com.reactive.api.challenge.appointment.usecases;

import com.reactive.api.challenge.appointment.repository.IAppointmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;
@Service
@AllArgsConstructor
public class DeleteAppointmentUseCase implements Function<String, Mono<Void>> {

    private final IAppointmentRepository appointmentRepository;

    @Override
    public Mono<Void> apply(String id) {
        return this.appointmentRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable("Appointment not found")))
                .flatMap(appointment -> this.appointmentRepository.deleteById(appointment.getId()))
                .onErrorResume(throwable -> Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())));
    }
}
