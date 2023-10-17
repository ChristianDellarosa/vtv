package com.vtv.inspection.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vtv.inspection.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_FORMAT);

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final String dateAsString = jsonParser.getText();
        if (dateAsString == null) {
            throw new IOException("ZonedDateTime argument is null.");
        }
        return LocalDateTime.parse(dateAsString, DATE_TIME_FORMATTER).atZone(DateUtils.getZoneId());
    }
}