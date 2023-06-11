package oogasalad.runner.model.exception;

/**
 * The NonexistentIDException is used to during the backup and revert stages to identify sets that
 * didn't exist previously and clean them accordingly.
 */
public class NonexistentIDException extends RuntimeException {

}
