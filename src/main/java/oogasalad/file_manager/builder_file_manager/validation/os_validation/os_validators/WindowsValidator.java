package oogasalad.file_manager.builder_file_manager.validation.os_validation.os_validators;

import java.util.ArrayList;
import java.util.List;

/**
 * The WindowsValidator class is responsible for checking Windows-specific conditions regarding
 * filenames, etc.
 *
 * @author alecliu
 */
public class WindowsValidator extends OSValidator {

  private static final String osAbbrev = "win";

  public WindowsValidator() {
    osName = osAbbrev;
    bannedCharacters = new ArrayList<>(
        List.of('\0', '\\', '/', ':', '*', '?', '\"', '<', '>', '|'));
  }
}
