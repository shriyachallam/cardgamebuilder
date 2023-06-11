package oogasalad.builder.backend.record;

import java.util.Map;

public record ComputerActionRecord(String name, Map<String, String> parameters) implements
    KeyValuePair {


    public String toVerboseString() {
        return "ComputerActionRecord{" +
                "name='" + name + '\'' +
                ", parameters=" + parameters +
                '}';
    }

    @Override
    public String toString() {
        return name + " " + parameters.toString();
    }
}
