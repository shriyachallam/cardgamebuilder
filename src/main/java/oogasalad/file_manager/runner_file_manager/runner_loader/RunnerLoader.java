package oogasalad.file_manager.runner_file_manager.runner_loader;

import java.util.List;
import oogasalad.file_manager.runner_file_manager.runner_loader.records.PlayerInfoRecord;
import oogasalad.runner.model.player.Player;
import oogasalad.runner.model.stage.Game;


/**
 * The RunnerLoader interface is responsible for fetching and loading games for the Game Runner
 * program. The RunnerLoader cooperates with the RunnerParser class initially to load in a Parser
 * representation from a file, then configure a ready-to-run game object.
 *
 * @author alecliu
 */
public interface RunnerLoader {


  /**
   * Load a game from a file.
   * Return a Game object that is ready to run.
   *    Includes Stages:
   *    * Game
   *    * Round(s)
   *    * Turn(s)
   *    * Phase(s)
   *        * Action(s)
   * If the file is not found, or the file is not a valid game file, throw a RuntimeException.
   * @param filename
   * @return
   * @throws RuntimeException
   *
   *
   */
  @Deprecated
  Game loadGame(String filename) throws RuntimeException;


  /**
   * Start a new game, pass in List of player records to configure the game.
   * @param filepath desired filepath
   * @param playerInfo record containing the player names and desired icons
   * @return configured game object
   * @throws RuntimeException if the file path or file is invalid
   */
  Game startNewGame(String filepath, List<PlayerInfoRecord> playerInfo) throws RuntimeException;

  /**
   * Save a game to a file. If the file is not found, or the file is not a valid game file, throw a
   * RuntimeException.
   *
   * @param game
   * @param filepath
   * @throws RuntimeException
   */
   void saveGame(Game game, String filepath);


  /**
   * Load a game from a saved game file. Recovers gameTemplate and boardRecord from the file.
   * boardRecord contains Players (including their hands) and the board state (groups of cards on the board).
   * @param filepath
   * @return
   */
   Game loadExistingGame(String filepath);



  /**
     * Update the language of messages returned by the parser.
     *
     * @param language desired language
     */
  void setLanguage(String language);

}
