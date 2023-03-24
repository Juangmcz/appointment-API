package com.reactive.api.challenge.appointment.router;

import com.reactive.api.challenge.appointment.domain.customer.CustomerDTO;
import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import com.reactive.api.challenge.appointment.usecases.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AppointmentRouter {

    private WebClient customerAPI;

    public AppointmentRouter(){
        customerAPI = WebClient.create("http://localhost:8081//api");
    }

    @Bean
    public RouterFunction<ServerResponse> getAllAppointments(GetAllAppointmentsUseCase getAllAppointmentsUseCase){
        return route(GET("api/appointments"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getAllAppointmentsUseCase.get(), AppointmentDTO.class))
                        .onErrorResume(throwable -> ServerResponse.noContent().build()));
    }

    @Bean
    public RouterFunction<ServerResponse> getAppointmentById(GetAppointmentByIdUseCase getAppointmentByIdUseCase){
        return route(GET("api/appointments/{id}"),
                request -> getAppointmentByIdUseCase.apply(request.pathVariable("id"))
                        .flatMap(appointmentDTO -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(appointmentDTO))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(throwable.getMessage())));
    }

    @Bean
    public RouterFunction<ServerResponse> saveAppointment(SaveAppointmentUseCase saveAppointmentUseCase){
        return route(POST("api/appointments").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(AppointmentDTO.class)
                        .flatMap(appointmentDTO -> saveAppointmentUseCase.apply(appointmentDTO)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).bodyValue(throwable.getMessage()))));
    }

    @Bean
    public RouterFunction<ServerResponse> updateAppointment(UpdateAppointmentUseCase updateAppointmentUseCase){
        return route(PUT("api/appointments/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(AppointmentDTO.class)
                        .flatMap(appointmentDTO -> updateAppointmentUseCase.apply(request.pathVariable("id"), appointmentDTO)
                                .flatMap(result -> ServerResponse.status(200)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).bodyValue(throwable.getMessage()))));
    }

    @Bean
    public RouterFunction<ServerResponse> deleteAppointment(DeleteAppointmentUseCase deleteAppointmentUseCase){
        return route(DELETE("api/appointments/{id}"),
                request ->  deleteAppointmentUseCase.apply(request.pathVariable("id"))
                        .flatMap(result -> ServerResponse.status(204)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(result))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).build()));
    }

    @Bean
    public RouterFunction<ServerResponse> scheduleAppointment(ScheduleAppointmentUseCase scheduleAppointmentUseCase){
        return route(POST("api/appointments/{id}/schedule/{id_c}"),
                request -> customerAPI.get()
                        .uri("/customers/"+request.pathVariable("id_c"))
                        .retrieve()
                        .bodyToMono(CustomerDTO.class)
                        .flatMap(customerDTO -> scheduleAppointmentUseCase
                                .schedule(request.pathVariable("id"),customerDTO.getId())
                                .flatMap(appointmentDTO -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(appointmentDTO))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(throwable.getMessage())))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(throwable.getMessage())));

    }
}
