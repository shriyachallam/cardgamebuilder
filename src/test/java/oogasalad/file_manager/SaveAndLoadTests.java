package oogasalad.file_manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import oogasalad.file_manager.runner_file_manager.runner_loader.DefaultLoader;
import oogasalad.runner.model.player.Player;
import oogasalad.runner.model.stage.Game;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class SaveAndLoadTests {


  private static final String USER_DIRECTORY = System.getProperty("user.dir");
  private static final String JSON_DIRECTORY = USER_DIRECTORY + "/data/json/";

  private DefaultLoader rummyLoader;
  @BeforeEach
  void setup() {

    rummyLoader = new DefaultLoader();


  }


  /**
   * Loads a game that was saved while in progress.
   * Checks that the game was loaded correctly by checking the number of players
   */
  @Test
  void testLoadSavedGame() {

    Game game = rummyLoader.loadExistingGame(JSON_DIRECTORY + "/test_files/SavedTestGame.json");
    assertEquals(2, game.getBoard().players().size());
  }


  /**
   * Loads a game with a non-existent action (BrokenTestGame.json)
   * Checks that factory throws a runtime error
   */
  @Test
  void testLoadBrokenGame() {
    assertThrows(RuntimeException.class, () -> rummyLoader.startNewGame(JSON_DIRECTORY + "BrokenTestGame.json", new ArrayList<>()));
  }


  /**
   * Loads gin rummy game, saves it, and checks that the saved file is the same as the expected file
   */
  @Test
  void testSaveGame() {
    Game game = rummyLoader.startNewGame(JSON_DIRECTORY + "games/ginrummy/GinRummyTest.json", new ArrayList<>());
    rummyLoader.saveGame(game, JSON_DIRECTORY + "/test_files/SaveAndLoaderTest.json");
    try{
      filesEqual(JSON_DIRECTORY + "/test_files/SaveAndLoaderTestExpected.json", JSON_DIRECTORY + "/test_files/SaveAndLoaderTest.json");
    } catch (Exception e) {
      fail(e.getMessage());
    }

  }




  void filesEqual(String filepath1, String filepath2) throws JSONException, IOException {
    String expectedJson = FileUtils.readFileToString(new File(filepath1));
    String actualJson = FileUtils.readFileToString(new File(filepath2));
    JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
  }


}
