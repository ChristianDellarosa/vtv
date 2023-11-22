package com.vtv.appointment.configuration;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

import static com.vtv.appointment.util.DateUtils.DATE_TIME_FORMAT;
import static com.vtv.appointment.util.DateUtils.getLocale;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(DATE_TIME_FORMAT);
            builder.serializers(new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, getLocale())));
        };
    }
}

