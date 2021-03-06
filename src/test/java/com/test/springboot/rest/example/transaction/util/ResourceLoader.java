package com.test.springboot.rest.example.transaction.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.springboot.rest.example.util.Unchecked;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ResourceLoader {

  private ObjectMapper mapper;

  public ResourceLoader(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public ObjectMapper getObjectMapper() {
    return this.mapper;
  }

  public String loadResourceContent(String path) {
    return Optional.of(path)
      .map(ResourceLoader.class.getClassLoader()::getResource)
      .map(Unchecked.function(URL::toURI))
      .map(Paths::get)
      .map(Unchecked.function(Files::readString))
      .orElse(null);

  }

  public <T> T loadResourceJsonObject(String path, Class<T> clazz) {
    return Optional.of(path)
      .map(this::loadResourceContent)
      .map(Unchecked.function(jsonString -> mapper.readValue(jsonString, clazz)))
      .orElse(null);
  }

  public <T> T loadJsonString(String content, Class<T> clazz) {
    return Optional.of(content)
      .map(Unchecked.function(jsonString -> mapper.readValue(jsonString, clazz)))
      .orElse(null);
  }

}
