package oogasalad.file_manager.builder_file_manager;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.file_manager.exceptions.NonexistentOptionException;

/**
 * The BuilderParser interface is meant to represent a general parser that can write a built game to
 * a file. The file format is abstract, and implemented by concrete classes.
 *
 * @author alecliu
 */
public interface BuilderFileManager {
  enum Category {
    CONDITION("conditions"),
    PLAYER_ACTION("player_actions"),
    COMPUTER_ACTION("computer_actions");

    private final String key;

    Category(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }
  }
  /**
   * Primary entry point from the Builder to write a user-made game to a file.
   *
   * @param filepath given filepath
   * @param gameRecord configured game object representation
   * @throws RuntimeException if a game is misconfigured
   */
  void saveGame(String filepath, GameRecord gameRecord);

  /**
   * Load a game template from a file into the builder for editing
   * @param filepath given filepath
   * @return loaded game template
   * @throws RuntimeException if the file does not exist or is invalid
   */
  GameRecord loadGameTemplate(String filepath) throws RuntimeException;

  /**
   * Update the language of messages returned by the parser.
   *
   * @param language desired language
   */
  void setLanguage(String language);

  /**
   * Fetch all available options for a category
   *
   * @param categoryType category type
   * @return list of available options
   */
  List<String> fetchOptions(Category categoryType);

  /**
   * Fetches a list of all required parameters for a particular option
   *
   * @param categoryType category type
   * @param optionName   list of required parameters
   * @return list of all required parameter names
   * @throws NonexistentOptionException if the provided option does not exist in the category
   */
  List<String> fetchParametersForOption(Category categoryType, String optionName)
      throws NonexistentOptionException;

  /**
   * Validates if a given option was provided with the correct input type
   *
   * @param optionName option name
   * @param parameters map of values to parameters
   * @throws RuntimeException if the given information is invalid
   */
  void validateOption(Category categoryType, String optionName, Map<String, String> parameters)
      throws RuntimeException;

}
