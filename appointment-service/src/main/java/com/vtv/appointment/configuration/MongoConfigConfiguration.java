package com.vtv.appointment.configuration;


import com.vtv.appointment.util.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoConfigConfiguration {

    @ReadingConverter
    public class ZonedDateTimeReadConverter implements Converter<Date, ZonedDateTime> {
        @Override
        public ZonedDateTime convert(Date date) {
            return date.toInstant().atZone(DateUtils.getZoneId());
        }
    }

    @WritingConverter
    public class ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, Date> {
        @Override
        public Date convert(ZonedDateTime zonedDateTime) {
            return Date.from(zonedDateTime.toInstant());
        }
    }
    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(new ZonedDateTimeReadConverter(), new ZonedDateTimeWriteConverter()));
    }
}
