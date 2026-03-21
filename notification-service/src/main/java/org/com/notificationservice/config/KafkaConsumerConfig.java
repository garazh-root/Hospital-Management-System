package org.com.notificationservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private final String bootstrapServers = "kafka:9092";

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JacksonJsonDeserializer.TYPE_MAPPINGS,
                        "doctorCreated:org.com.notificationservice.event.doctor.DoctorCreatedEvent," +
                        "doctorChangedStatus:org.com.notificationservice.event.doctor.DoctorChangedStatusEvent," +
                        "meetingBooked:org.com.notificationservice.event.meeting.MeetingBookedEvent," +
                        "meetingCancelled:org.com.notificationservice.event.meeting.MeetingCancelledEvent," +
                        "meetingCompleted:org.com.notificationservice.event.meeting.MeetingCompletedEvent," +
                        "patientCreated:org.com.notificationservice.event.patient.PatientCreatedEvent," +
                        "patientStatusUpdated:org.com.notificationservice.event.patient.PatientStatusUpdatedEvent");
        props.put("spring.json.trusted.packages", "*");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> consumerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        consumerFactory.setConsumerFactory(consumerFactory());
        return consumerFactory;
    }
}