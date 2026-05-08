package com.seb.mspedido.infrastructure.config.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JacksonJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;


@Configuration
public class KafkaConfig {
    @Bean
    public RecordMessageConverter recordMessageConverter() {
        // En Spring Kafka 4.0+, este conversor es lo suficientemente inteligente
        // para inferir el DTO basándose en el parámetro de tu método @KafkaListener
        // (siempre y cuando el productor no envíe el header __TypeId__)
        return new JacksonJsonMessageConverter();
    }
}
