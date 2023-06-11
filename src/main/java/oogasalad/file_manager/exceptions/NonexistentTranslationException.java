package oogasalad.file_manager.exceptions;

/**
 * The NonexistentOptionException is meant to differentiate the types of runtime exceptions thrown
 * by OptionTemplateList. This exception occurs specifically when a translation does not exist
 * within the language resource pack. This could occur due to misspelling. This exception is occurs
 * commonly with the NonexistentOptionException, as both can occur from the same root issue, but
 * this exception might also be thrown elsewhere.
 *
 * @author alecliu
 */
public class NonexistentTranslationException extends RuntimeException {

  public NonexistentTranslationException(String message) {
    super(message);
  }
}
