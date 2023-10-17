package com.vtv.inspection.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class JacksonConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper() //TODO: Ver como generar el bean
                .registerModule(new JavaTimeModule())
                .registerModule(new SimpleModule().addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer())));
//TODO: Esta verga te cambia tambien el listener, sin esto no funciona
    }
}
