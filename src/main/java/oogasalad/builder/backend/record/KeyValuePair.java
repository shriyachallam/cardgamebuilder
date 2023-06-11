package oogasalad.builder.backend.record;
/**
 *
 *
 *
 * Author: T. Geissler
 *
 *
 *
 */

import java.util.Map;

public interface KeyValuePair {

  String name();

  Map<String, String> parameters();
}
