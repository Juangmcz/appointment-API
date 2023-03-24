package com.reactive.api.challenge.appointment.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactive.api.challenge.appointment.config.RabbitConfig;
import com.reactive.api.challenge.appointment.domain.dto.AppointmentDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class AppointmentPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public AppointmentPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(AppointmentDTO appointmentDTO, String customerId) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(new AppointmentEvent(customerId, appointmentDTO, "schedule"));
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, message);
    }
}
