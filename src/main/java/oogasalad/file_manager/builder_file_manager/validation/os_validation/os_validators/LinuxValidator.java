package oogasalad.file_manager.builder_file_manager.validation.os_validation.os_validators;

import java.util.ArrayList;
import java.util.List;

/**
 * The LinuxValidator class is responsible for checking Linux-specific conditions regarding
 * filenames, etc.
 *
 * @author alecliu
 */
public class LinuxValidator extends OSValidator {

  private static final String osAbbrev = "linux";

  public LinuxValidator() {
    super();
    osName = osAbbrev;
    bannedCharacters = new ArrayList<>(
        List.of('\0', '/'));
  }
}
