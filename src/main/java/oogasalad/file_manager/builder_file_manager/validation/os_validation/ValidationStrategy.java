package oogasalad.file_manager.builder_file_manager.validation.os_validation;

import java.util.ArrayList;
import java.util.List;
import oogasalad.file_manager.builder_file_manager.validation.os_validation.os_validators.LinuxValidator;
import oogasalad.file_manager.builder_file_manager.validation.os_validation.os_validators.MacValidator;
import oogasalad.file_manager.builder_file_manager.validation.os_validation.os_validators.OSValidator;
import oogasalad.file_manager.builder_file_manager.validation.os_validation.os_validators.WindowsValidator;
import oogasalad.i18n.translators.Translator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The OSValidationStrategy class is responsible for checking OS-specific conditions to ensure that
 * the game will run smoothly.
 *
 * @author alecliu
 */
public class ValidationStrategy {

  private static final Logger LOG = LogManager.getLogger(ValidationStrategy.class);
  private static final List<OSValidator> SUPPORTED_SYSTEMS = new ArrayList<>(
      List.of(new MacValidator(), new WindowsValidator(), new LinuxValidator()));
  private final Translator translator;

  /**
   * Class constructor
   *
   * @param translator configured translator
   */
  public ValidationStrategy(Translator translator) {
    this.translator = translator;
  }

  /**
   * OS-specific validation strategy to ensure that each given game name will work on the current
   * operating system.
   *
   * @param gameName user inputted game name
   */
  // inspired by https://mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
  public void validateGameName(String gameName) throws RuntimeException {
    String operatingSystem = System.getProperty("os.name").toLowerCase();
    for (OSValidator validator : SUPPORTED_SYSTEMS) {
      if (operatingSystem.contains(validator.getOsName())) {
        if (!validator.validateFileName(gameName)) {
          String message = String.format(translator.translateToUserSpace("InvalidFilenameError"), gameName);
          LOG.error(message);
          throw new RuntimeException(message);
        }
        return;
      }
    }

    // No matching os_validators
    String message = translator.translateToUserSpace("UnsupportedOSError");
    LOG.error(message);
    throw new RuntimeException(message);
  }
}
