package ru.yandex.practicum.filmorate.jsontool;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationTypeAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        out.beginObject();

        out.value(value.toSeconds());

        out.endObject();
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        in.beginObject();

        var result = Duration.ofSeconds(in.nextLong());

        in.endObject();

        return result;
    }
}
