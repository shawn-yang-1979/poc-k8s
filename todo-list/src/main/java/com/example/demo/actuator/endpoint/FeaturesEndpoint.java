package com.example.demo.actuator.endpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "features")
public class FeaturesEndpoint {
  private Map<String, Boolean> features = new ConcurrentHashMap<>();

  @ReadOperation
  public Map<String, Boolean> readFeatures() {
    return features;
  }

  @ReadOperation
  public Boolean readFeature(@Selector String name) {
    return features.get(name);
  }

  @WriteOperation
  public void writeFeature(@Selector String name, Boolean value) {
    features.put(name, value);
  }

  @DeleteOperation
  public void deleteFeature(@Selector String name) {
    features.remove(name);
  }

}
