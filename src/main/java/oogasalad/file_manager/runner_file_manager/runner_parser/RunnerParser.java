package oogasalad.file_manager.runner_file_manager.runner_parser;

import oogasalad.builder.backend.record.GameRecord;
import oogasalad.file_manager.runner_file_manager.runner_parser.records.SavedGameRecord;
import oogasalad.runner.model.stage.Game;

/**
 * The RunnerParser interface is meant to represent a general parser that can read a built game from
 * a file. The file format is abstract, and implemented by concrete classes.
 *
 * @author alecliu
 */
public interface RunnerParser {

  /**
   * Loads a new GameRecord from template file. Does not include player or group information.
   * @param filepath provided file path
   * @return configured game
   * @throws RuntimeException if file is not found
   */
  GameRecord loadGameTemplate(String filepath) throws RuntimeException;


  /**
   * Load a GameRecord from a saved game file. Includes player and group information.
   * @param filepath provided file path
   * @return SavedGameRecord object (which contains GameRecord and board info).
   * @throws RuntimeException if file is not found
   */
  SavedGameRecord loadSavedGameRecord(String filepath) throws RuntimeException;

  /**
   * Save an in-progress game to a JSON file.
   *
   * @param game     in-progress game
   * @param filepath desired file path
   * @throws RuntimeException
   */
  void saveGame(Game game, String filepath) throws RuntimeException;

  /**
   * Update the language of messages returned by the parser.
   *
   * @param language desired language
   */
  void setLanguage(String language);
}
