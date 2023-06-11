package oogasalad.builder.backend.record;

import java.util.Map;

public record PlayerActionRecord(String name, Map<String, String> parameters) implements
    KeyValuePair {

  public String toVerboseString() {
    return "PlayerActionBuilder{" +
            "name='" + name + '\'' +
            ", params=" + parameters.toString() +
            '}';
  }

  @Override
  public String toString() {
    return name + " " + parameters.toString();
  }
}
