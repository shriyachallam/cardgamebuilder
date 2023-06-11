package oogasalad.file_manager.runner_file_manager.runner_parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Set;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.file_manager.runner_file_manager.runner_parser.records.CardGroupInterfaceAdapter;
import oogasalad.file_manager.runner_file_manager.runner_parser.records.CardInterfaceAdapter;
import oogasalad.file_manager.runner_file_manager.runner_parser.records.PlayerInterfaceAdapter;
import oogasalad.file_manager.runner_file_manager.runner_parser.records.SavedGameRecord;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.player.Player;
import oogasalad.runner.model.stage.Game;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The RunnerJSONParser class is responsible for creating Parser representations of games read from
 * JSON files. This class cooperates with the RunnerLoader classes to format the game to be ready to
 * run. The class uses the GSON library provided by Google.
 *
 * @author alecliu
 */
public class RunnerJSONParser implements RunnerParser {

  private static final String FILE_PREFIX = "/data";
  private static final Logger LOG = LogManager.getLogger(RunnerJSONParser.class);
  private final Translator translator;

  /**
   * Default class constructor.
   */
  public RunnerJSONParser() {
    translator = new DefaultTranslator(AppType.FILE_MANAGER);
  }

  /**
   * Class constructor.
   *
   * @param language desired language
   */
  public RunnerJSONParser(String language) {
    translator = new DefaultTranslator(AppType.FILE_MANAGER, language);
  }

  /**
   * Class constructor
   *
   * @param translator pre-configured translator
   */
  public RunnerJSONParser(Translator translator) {
    this.translator = translator;
  }

  /**
   * Load a Parser representation of an existing game from a JSON file
   *
   * @param filepath provided file path
   * @return configured game
   */
  @Override
  public GameRecord loadGameTemplate(String filepath) throws RuntimeException {
    try {
      Gson g = new Gson();
      FileReader fr = new FileReader(filepath);
      GameRecord gameRecord = g.fromJson(fr, GameRecord.class);
      LOG.info(String.format(translator.translateToUserSpace("SuccessfulReadFromFile"),
          getProjectFilepath(filepath)));
      return gameRecord;
    } catch (FileNotFoundException fileNotFoundException) {
      String message = String.format(translator.translateToUserSpace("FileNotFoundError"),
          getProjectFilepath(filepath));
      LOG.error(message);
      throw new RuntimeException(message);
    }
  }

  /**
   * Load a GameRecord from a saved game file. Includes player and group information.
   *
   * @param filepath provided file path
   * @return SavedGameRecord object (which contains GameRecord and board info).
   * @throws RuntimeException if file is not found
   */
  @Override
  public SavedGameRecord loadSavedGameRecord(String filepath) throws RuntimeException {
    try {
      GsonBuilder gBuilder = new GsonBuilder();
      gBuilder.registerTypeAdapter(CardGroup.class, new CardGroupInterfaceAdapter());
      gBuilder.registerTypeAdapter(Card.class, new CardInterfaceAdapter());
      gBuilder.registerTypeAdapter(Player.class, new PlayerInterfaceAdapter());
      Gson g = gBuilder.create();
      FileReader fr = new FileReader(filepath);
      SavedGameRecord savedGameRecord = g.fromJson(fr, SavedGameRecord.class);
      LOG.info(String.format(translator.translateToUserSpace("SuccessfulReadFromFile"),
          getProjectFilepath(filepath)));
      return savedGameRecord;
    } catch (FileNotFoundException fileNotFoundException) {
      String message = String.format(translator.translateToUserSpace("FileNotFoundError"),
          getProjectFilepath(filepath));
      LOG.error(message);
      throw new RuntimeException(message);
    }
  }

  /**
   * Save an in-progress game to a JSON file.
   *
   * @param game     in-progress game
   * @param filepath desired file path
   * @throws RuntimeException
   */
  @Override
  public void saveGame(Game game, String filepath) throws RuntimeException {
    //Can we save card groups with label of type/who owns it (board, deck, hand)
    List<Player> players = game.getBoard().players();
    Set<CardGroup> groups = game.getBoard().groups();

    for(Player player : players){
      if(groups.contains(player.hand())){
        groups.remove(player.hand());
      }
    }

    int stepCounter = game.getStepCounter();

    SavedGameRecord savedGameRecord = new SavedGameRecord(game.getSavedGameTemplate(),
        players, groups, stepCounter);

    try {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(CardGroup.class, new CardGroupInterfaceAdapter());
      builder.registerTypeAdapter(Card.class, new CardInterfaceAdapter());
      builder.registerTypeAdapter(Player.class, new PlayerInterfaceAdapter());
      Gson g = builder.create();
      FileWriter writer = new FileWriter(filepath);
      g.toJson(savedGameRecord, writer);
      writer.close();
      LOG.info(String.format(translator.translateToUserSpace("SuccessfulWriteToFile"),
          getProjectFilepath(filepath)));
    } catch (Exception e) {
      LOG.error("Error saving game: " + e.getMessage());
    }
  }

  /**
   * Update the language of messages returned by the parser.
   *
   * @param language desired language
   */
  @Override
  public void setLanguage(String language) {
    translator.setLanguage(language);
  }

  /**
   * Cuts the file path so that the displayed and logged filepath do not include personal directory
   * paths
   *
   * @param filepath given filepath
   * @return project filepath
   */
  private String getProjectFilepath(String filepath) {
    int index = filepath.indexOf(FILE_PREFIX);
    if (index < 0) {
      return filepath;
    }
    return filepath.substring(index);
  }
}
