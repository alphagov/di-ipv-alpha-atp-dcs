package uk.gov.di.ipv.atp.dcs.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class InstantShortDateAdapter extends TypeAdapter<Instant> {

    private static final String SHORT_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern(SHORT_FORMAT)
        .withZone(ZoneId.from(ZoneOffset.UTC));

    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        if (value != null) {
            out.value(FORMATTER.format(value));
        }

        out.flush();
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            throw new RuntimeException("Failed to deserialize date, null token provided");
        }

        var next = in.nextString();
        return FORMATTER.parse(next, Instant::from);
    }
}
