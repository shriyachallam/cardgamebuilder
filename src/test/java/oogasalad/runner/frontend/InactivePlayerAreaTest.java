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
import oogasalad.runner.view.player_area.ActivePlayerArea;
import oogasalad.runner.view.player_area.InactivePlayerArea;
import oogasalad.service.Firebase;
import oogasalad.util.DukeApplicationTest;
import org.junit.jupiter.api.Test;

public class InactivePlayerAreaTest extends DukeApplicationTest {

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
    Game game=runnerLoader.loadExistingGame(JSON_DIRECTORY + "games/gin/saved_examples/SpoonsTest.json");
    GameMainScene view = new GameMainScene(translator, game, runnerLoader);
    cardManager = new CardViewManager(view, translator, new Firebase());
    game.getBoard().register(cardManager);
    game.step();
    stage.setScene(view.getScene());
    return null;
  }

  @Test
  void testSyncedUpActiveInactivePlayerViews() {
    clickOn("#playerTransitionButton");
    sleep(3000, TimeUnit.MILLISECONDS);
    assertEquals(1,lookup("#1233playerIcon").queryAll().size());
  }
}