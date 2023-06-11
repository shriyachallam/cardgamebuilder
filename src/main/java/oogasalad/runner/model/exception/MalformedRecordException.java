package oogasalad.runner.model.exception;

/**
 * An exception that is thrown when a record cannot be parsed into a game object.
 */
public class MalformedRecordException extends RuntimeException {

  public MalformedRecordException(String message) {
    super(message);
  }

}
