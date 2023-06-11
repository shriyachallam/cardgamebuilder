package oogasalad.file_manager.builder_file_manager.validation.os_validation.os_validators;

import java.util.ArrayList;
import java.util.List;

/**
 * The MacValidator class is responsible for checking Mac-specific conditions regarding filenames,
 * etc.
 *
 * @author alecliu
 */
public class MacValidator extends OSValidator {

  private static final String osAbbrev = "mac";

  public MacValidator() {
    super();
    osName = osAbbrev;
    bannedCharacters = new ArrayList<>(List.of(':', '/'));
  }
}
