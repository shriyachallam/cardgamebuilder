package oogasalad.file_manager.exceptions;


/**
 * The NonexistentOptionException is meant to differentiate the types of runtime exceptions thrown
 * by OptionTemplateList. This exception occurs specifically when an option does not exist within
 * the specified category. This could occur due to misspelling.
 *
 * @author alecliu
 */
public class NonexistentOptionException extends RuntimeException {

  /**
   * Default class constructor
   */
  public NonexistentOptionException() {
    super();
  }
}
