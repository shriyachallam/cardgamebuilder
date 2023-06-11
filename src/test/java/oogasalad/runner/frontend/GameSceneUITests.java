package oogasalad.runner.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.file_manager.runner_file_manager.runner_loader.DefaultLoader;
import oogasalad.file_manager.runner_file_manager.runner_loader.RunnerLoader;
import oogasalad.file_manager.runner_file_manager.runner_loader.records.PlayerInfoRecord;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.model.player.Player;
import oogasalad.runner.model.stage.Game;
import oogasalad.runner.view.CardViewManager;
import oogasalad.runner.view.game_views.GameMainScene;
import oogasalad.service.Firebase;
import oogasalad.util.DukeApplicationTest;
import org.junit.jupiter.api.Test;

public class GameSceneUITests extends DukeApplicationTest {

  private static final String USER_DIRECTORY = System.getProperty("user.dir");
  private static final String JSON_DIRECTORY = USER_DIRECTORY + "/data/json/";
  private CardViewManager cardManager;
  private Stage stage;
  private RunnerLoader runnerLoader;
  private Translator translator;
  @Override
  public void start(Stage stage) {
    this.stage = stage;
    runnerLoader = new DefaultLoader("English");
    translator = new DefaultTranslator(AppType.RUNNER, "English");
    beginGame(List.of(new PlayerInfoRecord("Player1", 0), new PlayerInfoRecord("Player2", 1), new PlayerInfoRecord("Player3", 2)));
    stage.show();
  }

  private Void beginGame(List<PlayerInfoRecord> playerInfoRecords) {
    Game game=runnerLoader.loadExistingGame(JSON_DIRECTORY + "games/gin/saved_examples/TestGame");
    GameMainScene view = new GameMainScene(translator, game, runnerLoader);
    cardManager = new CardViewManager(view, translator, new Firebase());
    game.getBoard().register(cardManager);
    game.step();
    stage.setScene(view.getScene());
    return null;
  }

  @Test
  void testStart() {
    sleep(3000, TimeUnit.MILLISECONDS);
  }

  @Test
  void testPlayerTransition(){
    clickOn("#playerTransitionButton");
    sleep(5000, TimeUnit.MILLISECONDS);
    assertFalse(lookup("#playerTransitionPane").query().isVisible());
  }

  @Test
  void testCardSelection(){
    clickOn("#playerTransitionButton");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn("#cardCLUB1");
    sleep(500, TimeUnit.MILLISECONDS);
    assertTrue(lookup("#cardCLUB1").query().getStyleClass().contains("selected-card"));
  }

  @Test
  void testGroupSelection(){
    clickOn("#playerTransitionButton");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn("#cardCLUB1");
    clickOn("#cardCLUB1");
    sleep(500, TimeUnit.MILLISECONDS);
    assertTrue(lookup("#card-group1394634384").query().getStyleClass().contains("selected-group"));
  }

  @Test
  void testCardDraw(){
    clickOn("#playerTransitionButton");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn("#cardCLUB1");
    clickOn("#cardCLUB1");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn("#cardDIAMOND6");
    clickOn("#cardDIAMOND6");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn(((VBox)lookup("#actionButtonBox").query()).getChildren().get(0));
    sleep(500, TimeUnit.MILLISECONDS);
    assertEquals(8, ((Pane) lookup("#card-group1394634384").query()).getChildren().size());
  }

  @Test
  void testCompleteTurn(){
    clickOn("#playerTransitionButton");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn("#cardDIAMOND6");
    clickOn("#cardDIAMOND6");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn("#cardCLUB1");
    clickOn("#cardCLUB1");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn(((VBox)lookup("#actionButtonBox").query()).getChildren().get(0));
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn(((VBox)lookup("#actionButtonBox").query()).getChildren().get(2));
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn(((VBox)lookup("#actionButtonBox").query()).getChildren().get(2));
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn("#cardHEART6");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn("#cardHEART6");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn("#cardHEART4");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn("#cardCLUB8");
    clickOn("#cardCLUB8");
    sleep(500, TimeUnit.MILLISECONDS);
    clickOn(((VBox)lookup("#actionButtonBox").query()).getChildren().get(0));
    clickOn(((VBox)lookup("#actionButtonBox").query()).getChildren().get(1));
    sleep(500, TimeUnit.MILLISECONDS);
    assertTrue(lookup("#playerTransitionPane").query().isVisible());
    clickOn("#playerTransitionButton");
  }






}
