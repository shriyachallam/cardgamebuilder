package oogasalad.runner.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.file_manager.runner_file_manager.runner_loader.DefaultLoader;
import oogasalad.file_manager.runner_file_manager.runner_loader.RunnerLoader;
import oogasalad.file_manager.runner_file_manager.runner_loader.records.PlayerInfoRecord;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.model.stage.Game;
import oogasalad.runner.view.CardViewManager;
import oogasalad.runner.view.game_views.GameMainScene;
import oogasalad.runner.view.player_selection_scene.PlayerSelectionScene;
import oogasalad.util.DukeApplicationTest;
import org.junit.jupiter.api.Test;

public class PlayerSelectionUITests extends DukeApplicationTest {

  private static final String USER_DIRECTORY = System.getProperty("user.dir");
  private static final String JSON_DIRECTORY = USER_DIRECTORY + "/data/json/";
  private Stage stage;
  private RunnerLoader runnerLoader;
  private Translator translator;
  @Override
  public void start(Stage stage) {
    this.stage = stage;
    runnerLoader = new DefaultLoader("English");
    translator = new DefaultTranslator(AppType.RUNNER, "English");

    PlayerSelectionScene playerSelectionScene = new PlayerSelectionScene(translator,this::beginGame);
    stage.setScene(playerSelectionScene);
    stage.show();
  }

  private Void beginGame(List<PlayerInfoRecord> playerInfoRecords) {

    Game game=runnerLoader.startNewGame(JSON_DIRECTORY + "games/ginrummy/GinRummyTest.json", playerInfoRecords);
    GameMainScene view = new GameMainScene(translator, game, runnerLoader);
    CardViewManager cardManager = new CardViewManager(view, translator, null);
    game.getBoard().register(cardManager);
    game.step();
    stage.setScene(view.getScene());
    return null;
  }

  @Test
  void testStart() {
    sleep(1000, TimeUnit.MILLISECONDS);
  }

  @Test
  void testNoPlayerError() {
      clickOn("#ContinueButton");
      sleep(1000, TimeUnit.MILLISECONDS);
      assertTrue(lookup("#noPlayerError").query().isVisible());
  }

  @Test
  void testDuplicatePlayerError() {
    writeInputTo(lookup("#playerNameField").query(), "Player1");
    clickOn("#createPlayerButton");
    sleep(250, TimeUnit.MILLISECONDS);
    writeInputTo(lookup("#playerNameField").query(), "Player1");
    clickOn("#createPlayerButton");
    sleep(250, TimeUnit.MILLISECONDS);
    assertTrue(lookup("#duplicateNameError").query().isVisible());
  }

  @Test
  void testNoNameError(){
    clickOn("#createPlayerButton");
    sleep(250, TimeUnit.MILLISECONDS);
    assertTrue(lookup("#noNameError").query().isVisible());
  }
  @Test
  void testPlayerCreation() {
    writeInputTo(lookup("#playerNameField").query(), "Player1");
    clickOn("#createPlayerButton");
    sleep(250, TimeUnit.MILLISECONDS);
    assertEquals(1, lookup("#playerIcon").queryAll().size());
  }

  @Test
  void testIconSelection() {
    clickOn("#selectableIcon1");
    sleep(1000, TimeUnit.MILLISECONDS);
    assertEquals(((ImageView)((VBox)lookup("#selectableIcon1").query()).getChildren().get(0)).getImage(),((ImageView) lookup("#selectedPlayerIcon").query()).getImage());
  }
}
