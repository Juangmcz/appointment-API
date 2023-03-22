package com.reactive.api.challenge.appointment.repository;

import com.reactive.api.challenge.appointment.domain.collection.Appointment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAppointmentRepository extends ReactiveMongoRepository<Appointment, String> {
}
