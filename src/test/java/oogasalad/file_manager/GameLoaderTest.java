package oogasalad.file_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import oogasalad.file_manager.runner_file_manager.runner_loader.DefaultLoader;
import oogasalad.file_manager.runner_file_manager.runner_loader.RunnerLoader;
import oogasalad.file_manager.runner_file_manager.runner_loader.records.PlayerInfoRecord;
import oogasalad.i18n.AppType;
import oogasalad.i18n.TranslationPackage;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.model.board.PlayerBoard;
import oogasalad.runner.model.player.ConcretePlayer;
import oogasalad.runner.model.player.Player;
import oogasalad.runner.model.stage.Game;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameLoaderTest {


  private static final String USER_DIRECTORY = System.getProperty("user.dir");
  private static final String JSON_DIRECTORY = USER_DIRECTORY + "/data/json/";
  private RunnerLoader runnerLoader;
  private Game game;
  private PlayerBoard board;
  List<Player> players = new ArrayList<>();



  @BeforeEach
  void setup() {
    runnerLoader = new DefaultLoader();


    players.add(new ConcretePlayer("shriya"));
    players.add(new ConcretePlayer("del"));
    players.add(new ConcretePlayer("ken"));

    // game = runnerLoader.startNewGame(JSON_DIRECTORY + "GinRummyTest.json", players);
    //Game game = runnerLoader.loadGame(JSON_DIRECTORY + "GinRummyTest.json");

  }


  /**
   * Tests that game.step() will throw runtime error if .act() fails for a computer action
   * Loads a game with a bad before action (BadABeforeAction.json)
   * Calls game.step() to call .act() on the before actions
   * Checks that the error message is correct
   *
   * @Author Ken Kalin
   */
  @Test
  void testBadBeforeAction() {

    String language = "english";

    RunnerLoader loader = new DefaultLoader(language);
    game = loader.startNewGame(JSON_DIRECTORY + "/test_files/save_and_load_files/BadBeforeAction.json", new ArrayList<>());

    Exception exception = assertThrows(RuntimeException.class, () -> game.step());

    Translator errorTranslator = new DefaultTranslator(AppType.RUNNER,language);

    String expected = errorTranslator.translateToUserSpace(new TranslationPackage("PlayerActionError","class oogasalad.runner.model.action.computer.BadAction"));
    String actual = exception.getMessage();

    assertEquals(expected, actual);

  }


  /**
   * Tests that game.step() will throw runtime error if .act() fails for a computer action
   * Tests that the error message is translated correctly
   */
  @Test
  void testBadBeforeActionLanguage() {

    String language = "german";

    RunnerLoader loader = new DefaultLoader(language);

    game = loader.startNewGame(JSON_DIRECTORY + "/test_files/save_and_load_files/BadBeforeAction.json", new ArrayList<>());

    Exception exception = assertThrows(RuntimeException.class, () -> game.step());

    Translator errorTranslator = new DefaultTranslator(AppType.RUNNER,language);

    String expected = errorTranslator.translateToUserSpace(new TranslationPackage("PlayerActionError","class oogasalad.runner.model.action.computer.BadAction"));
    String actual = exception.getMessage();

    assertEquals(expected, actual);
  }

  /**
   * Tests that a gin rummy test game is loaded correctly
   * Step through the game and check that the board is updated correctly
   */
  @Test
  void testGinRummyLoad() {
    List<PlayerInfoRecord> playerInfoRecords = new ArrayList<>();
    playerInfoRecords.add(new PlayerInfoRecord("shriya", 0));
    playerInfoRecords.add(new PlayerInfoRecord("shriya1", 1));
    playerInfoRecords.add(new PlayerInfoRecord("shriya2", 2));
    playerInfoRecords.add(new PlayerInfoRecord("shriya3", 3));
    playerInfoRecords.add(new PlayerInfoRecord("shriya4", 4));
    game = runnerLoader.startNewGame(JSON_DIRECTORY + "/games/ginrummy/GinRummyTest.json", playerInfoRecords);
    board = game.getBoard();

    assertEquals(5, board.players().size());
    assertEquals(5, board.groups().size());
    assertEquals(0, board.allowedMoves().size());
    game.step();
    assertEquals(5, board.players().size());
    board.players().forEach(player -> {
      if (player.name().equals("shriya")) {
        assertTrue(player.isActive());
      } else {
        assertFalse(player.isActive());
      }
    });
    assertEquals(7, board.groups().size());
    board.groups().forEach(group -> {
      switch (group.tag()) {
        case "hand" -> assertEquals(7, group.size());
        case "discard" -> assertEquals(1, group.size());
        case "deck" -> assertEquals(16, group.size());
      }
    });
    assertEquals(2, board.allowedMoves().size());
    assertEquals(new TranslationPackage("Draw", "deck", "own"), board.allowedMoves().get(0).description());
    assertEquals(new TranslationPackage("Transfer", "discard", "own"), board.allowedMoves().get(1).description());
    game.step();
    assertEquals(2, board.allowedMoves().size());
    assertEquals(new TranslationPackage("CreateGroup", "face_up", "sequence", "set"), board.allowedMoves().get(0).description());
    assertEquals(new TranslationPackage("Transfer", "own", "set"), board.allowedMoves().get(1).description());
    game.step();
    assertEquals(1, board.allowedMoves().size());
    assertEquals(new TranslationPackage("Transfer", "own", "discard"), board.allowedMoves().get(0).description());
    game.step();
    board.players().forEach(player -> {
      if (player.name().equals("shriya1")) {
        assertTrue(player.isActive());
      } else {
        assertFalse(player.isActive());
      }
    });
    assertEquals(2, board.allowedMoves().size());
    assertEquals(new TranslationPackage("Draw", "deck", "own"), board.allowedMoves().get(0).description());
    assertEquals(new TranslationPackage("Transfer", "discard", "own"), board.allowedMoves().get(1).description());
  }
}

