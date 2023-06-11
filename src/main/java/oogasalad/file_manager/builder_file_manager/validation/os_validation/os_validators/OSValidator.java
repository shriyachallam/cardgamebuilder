package oogasalad.file_manager.builder_file_manager.validation.os_validation.os_validators;

import java.util.List;

/**
 * The OSValidator class is responsible for checking valid inputs for one OS.
 *
 * @author alecliu
 */
public abstract class OSValidator {

  protected String osName;
  protected List<Character> bannedCharacters;

  /**
   * Getter method
   *
   * @return OS name
   */
  public String getOsName() {
    return osName;
  }

  /**
   * Validates the filename by checking for OS-specific banned characters
   *
   * @param filename given filename
   * @return if the filename is valid
   */
  public boolean validateFileName(String filename) {
    if (filename == null || filename.length() == 0) {
      return false;
    }
    for (Character ch : bannedCharacters) {
      if (filename.indexOf(ch) > -1) {
        return false;
      }
    }
    return true;
  }
}
