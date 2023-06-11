package oogasalad.file_manager.exceptions;

/**
 * The InvalidParameterException is meant to differentiate the types of runtime exceptions thrown by
 * OptionTemplateList. This exception occurs specifically when an option (like "Create Group")
 * exists, but the given value is the wrong type.
 *
 * @author alecliu
 */
public class InvalidParameterException extends RuntimeException {

  /**
   * Default class constructor
   */
  public InvalidParameterException(String invalidParameterName) {
    super(invalidParameterName);
  }
}
