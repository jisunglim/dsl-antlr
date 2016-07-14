package me.jisung.pojos.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import me.jisung.pojos.Conclusion;

/**
 * Created by Jisung on 7/12/2016.
 */
public class ConclusionSerializer extends JsonSerializer<Conclusion> {
  @Override
  public void serialize(Conclusion conclusion,
                        JsonGenerator jsonGenerator,
                        SerializerProvider serializerProvider) throws IOException {

    jsonGenerator.writeString(conclusion.getName());
  }
}
