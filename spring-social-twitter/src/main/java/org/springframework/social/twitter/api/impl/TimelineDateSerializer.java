package org.springframework.social.twitter.api.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Serializer to write date values in Twitter timeline date format.
 * @author Vladimir Bogomolov
 */

public class TimelineDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString(new SimpleDateFormat(TIMELINE_DATE_FORMAT, Locale.ENGLISH).format(value));
    }

    private static final String TIMELINE_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
}
