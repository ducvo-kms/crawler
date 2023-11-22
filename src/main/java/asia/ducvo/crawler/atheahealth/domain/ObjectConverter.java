package asia.ducvo.crawler.atheahealth.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ObjectConverter implements AttributeConverter<Object, String> {

  private ObjectMapper mapper = new ObjectMapper();
  @Override
  public String convertToDatabaseColumn(Object attribute) {
    try {
      return mapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object convertToEntityAttribute(String dbData) {
    try {
      return mapper.readTree(dbData);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
