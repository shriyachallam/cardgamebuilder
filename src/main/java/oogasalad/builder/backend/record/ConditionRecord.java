package oogasalad.builder.backend.record;

import java.util.Map;

public record ConditionRecord(String name, Map<String, String> parameters) implements KeyValuePair {

  public String toVerboseString() {
    return "ConditionRecord{" +
            "name='" + name + '\'' +
            ", params=" + parameters.toString() +
            '}';
  }

  @Override
  public String toString() {
    return name + " " + parameters.toString();
  }
}
