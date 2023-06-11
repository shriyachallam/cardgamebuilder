package oogasalad.file_manager.exceptions;

/**
 * The IncorrectNumberOfParametersException is meant to differentiate the types of runtime
 * exceptions thrown by OptionTemplateList. This exception occurs specifically when there are too
 * few or too many provided parameters for an option.
 *
 * @author alecliu
 */
public class IncorrectNumberOfParametersException extends RuntimeException {

  public IncorrectNumberOfParametersException(String correctNumber) {
    super(correctNumber);
  }
}
