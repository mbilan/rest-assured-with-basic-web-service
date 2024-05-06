package request.objectmapper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class LocalDateTypeAdapter
    implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

  private final DateTimeFormatter formatter =
      new DateTimeFormatterBuilder()
          .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
          .appendFraction(ChronoField.NANO_OF_SECOND, 4, 9, true)
          .toFormatter();

  @Override
  public LocalDateTime deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    return LocalDateTime.parse(jsonElement.getAsString(), formatter);
  }

  @Override
  public JsonElement serialize(
      LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(localDateTime.format(formatter));
  }
}
