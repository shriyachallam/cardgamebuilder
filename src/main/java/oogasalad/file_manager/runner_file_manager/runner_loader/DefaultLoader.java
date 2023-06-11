package oogasalad.file_manager.runner_file_manager.runner_loader;

import java.util.List;
import java.util.Set;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.file_manager.runner_file_manager.runner_loader.records.PlayerInfoRecord;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;
import oogasalad.file_manager.runner_file_manager.runner_parser.RunnerJSONParser;
import oogasalad.file_manager.runner_file_manager.runner_parser.RunnerParser;
import oogasalad.file_manager.runner_file_manager.runner_parser.records.SavedGameRecord;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.player.ConcretePlayer;
import oogasalad.runner.model.player.Player;
import oogasalad.runner.model.stage.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The DefaultLoader class is responsible for translating a parsed game file into a configured
 * game-runnable structure. The DefaultLoader class is built to specifically manage Rummy-like
 * rules.
 *
 * @author alecliu kenkalin
 */
public class DefaultLoader implements RunnerLoader {

  private static final Logger LOG = LogManager.getLogger(DefaultLoader.class);
  private RunnerParser runnerParser;
  private Translator translator;

  /**
   * Class constructor
   */
  public DefaultLoader() {
    translator = new DefaultTranslator(AppType.FILE_MANAGER);
    runnerParser = new RunnerJSONParser();
  }

  /**
   * Class constructor
   *
   * @param language desired language
   */
  public DefaultLoader(String language) {
    translator = new DefaultTranslator(AppType.FILE_MANAGER, language);
    runnerParser = new RunnerJSONParser(translator);
  }


  /**
   * Load a game from a template file. Return a Game object that is ready to run. Includes Stages: *
   * Game * Round(s) * Turn(s) * Phase(s) * Action(s) I If the file is not found, or the file is not
   * a valid game file, throw a RuntimeException.
   *
   * @param filepath
   * @return
   * @throws RuntimeException
   */

  @Deprecated
  public Game loadGame(String filepath) throws RuntimeException {
    GameRecord game = runnerParser.loadGameTemplate(filepath);
    try {
      return new Game(game, new DefaultTranslator(AppType.RUNNER, translator.getLanguage()));
    } catch (RuntimeException runtimeException) {
      String message = String.format(translator.translateToUserSpace("GameConfigurationError"),
          runtimeException.getMessage());
      LOG.error(message);
      throw new RuntimeException(message);
    }
  }

  /**
   * Start a new game, pass in List of players, and number of copies of the deck to be included.
   *
   * @param filepath   desired filepath
   * @param playerInfo record containing the player names and desired icons
   * @return configured game object
   * @throws RuntimeException if the file path or file is invalid
   */
  @Override
  public Game startNewGame(String filepath, List<PlayerInfoRecord> playerInfo)
      throws RuntimeException {
    GameRecord gameRecord = runnerParser.loadGameTemplate(filepath);

    try {
      ConcreteBoard board = new ConcreteBoard();

      //Add players to board
      for (PlayerInfoRecord playerInfoRecord : playerInfo) {
        board.addPlayer(new ConcretePlayer(playerInfoRecord.name(), playerInfoRecord.iconId()));
      }
      Game game = new Game(gameRecord, board, false, new DefaultTranslator(AppType.RUNNER,
          translator.getLanguage()));
      return game;

    } catch (RuntimeException runtimeException) {
      String message = String.format(translator.translateToUserSpace("GameConfigurationError"),
          runtimeException.getMessage());
      LOG.error(message);
      throw new RuntimeException(message);
    }
  }

  /**
   * Update the language of messages returned by the parser.
   *
   * @param language desired language
   */
  @Override
  public void setLanguage(String language) {
    runnerParser.setLanguage(language);
  }


  /**
   * Save the current state of the game to a file. This includes the current board state and the
   * current template file. Will be stored as one combined file.
   *
   * @param game     game to save
   * @param filepath desired filepath
   */
  public void saveGame(Game game, String filepath) {
    runnerParser.saveGame(game, filepath);
  }

  /**
   * Load a game from a saved game file. Recovers gameTemplate and boardRecord from the file.
   * boardRecord contains Players (including their hands) and the board state (groups of cards on
   * the board).
   *
   * @param filepath
   * @return
   */
  @Override
  public Game loadExistingGame(String filepath) {
    SavedGameRecord savedGameRecord = runnerParser.loadSavedGameRecord(filepath);

    List<Player> players = savedGameRecord.players();
    Set<CardGroup> groups = savedGameRecord.groups();
    ConcreteBoard board = new ConcreteBoard();

    int modifiedSteps = adjustPlayerOrder(savedGameRecord, players);

    for (Player p : players) {
      board.addPlayer(p);
    }
    for (CardGroup g : groups) {
      board.addGroup(g);
    }
    Game game = new Game(savedGameRecord.gameTemplate(), board, true,
        new DefaultTranslator(AppType.RUNNER, translator.getLanguage()));

    for (int i = 0; i < modifiedSteps - 1; i++) {
      game.step();
    }

    return game;

  }

  /**
   * Adjusts the player order such that the last active player is queued first (game setup
   * standard). Then it adjusts the step counter to maintain player synchronization.
   *
   * @param savedGameRecord saved game record
   * @param players list of active players
   * @return the modified steps need to maintain synchronization
   */
  private int adjustPlayerOrder(SavedGameRecord savedGameRecord, List<Player> players) {
    int modifiedSteps = savedGameRecord.stepCounter();
    int stepsPerTurn = savedGameRecord.gameTemplate().stages().get(0).stages().get(0).stages()
        .size();
    int activeIndex = 0;
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).isActive()) {
        activeIndex = i;
        break;
      }
    }
    modifiedSteps -= activeIndex * stepsPerTurn;

    while (!players.get(0).isActive()) {
      Player firstPlayer = players.remove(0);
      players.add(firstPlayer);
    }
    return modifiedSteps;
  }
}
