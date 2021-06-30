package uk.gov.di.ipv.atp.dcs.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class InstantConverter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    private static final String LONG_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern(LONG_PATTERN)
        .withZone(ZoneId.from(ZoneOffset.UTC));

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(FORMATTER.format(src));
    }

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return FORMATTER.parse(json.getAsString(), Instant::from);
    }
}
