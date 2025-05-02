package com.example.demo.actuator.endpoint;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
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
  private Map<String, Feature> features = new ConcurrentHashMap<>();

  @ReadOperation
  public Collection<Feature> readFeatures() {
    return features.values();
  }

  @ReadOperation
  public Optional<Feature> readFeature(@Selector String name) {
    return Optional.ofNullable(features.get(name));
  }

  @WriteOperation
  public void writeFeature(@Selector String name, boolean value) {
    Feature feature = new Feature();
    feature.setName(name);
    feature.setEnabled(value);
    features.put(name, feature);
  }

  @DeleteOperation
  public void deleteFeature(@Selector String name) {
    features.remove(name);
  }

  public static class Feature {
    private String name;
    private boolean enabled;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }
  }
}
