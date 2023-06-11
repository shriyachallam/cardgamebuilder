package oogasalad.runner.controller;


import java.util.List;
import javafx.application.Application;
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
import oogasalad.service.Firebase;


public class RunnerMain extends Application {

  /**
   * Initializes the scene controller
   */

  private static final String USER_DIRECTORY = System.getProperty("user.dir");
  private static final String JSON_DIRECTORY = USER_DIRECTORY + "/data/json/";
  private Stage stage;
  private RunnerLoader runnerLoader;
  private Translator translator;
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    this.stage = stage;
    runnerLoader = new DefaultLoader("English");
    translator = new DefaultTranslator(AppType.RUNNER, "English");

    PlayerSelectionScene playerSelectionScene = new PlayerSelectionScene(translator, this::beginGame);
    stage.setScene(playerSelectionScene);
    stage.show();
  }

  private Void beginGame(List<PlayerInfoRecord> playerInfoRecords) {
   // Game game=runnerLoader.startNewGame(JSON_DIRECTORY + "/games/gin/Gin.json", playerInfoRecords);
    Game game=runnerLoader.startNewGame(JSON_DIRECTORY + "games/gin/saved_examples/GinMultTurns.json", playerInfoRecords);
    //Game game=runnerLoader.loadExistingGame(JSON_DIRECTORY + "AlmostGin.json");

    //Game game=runnerLoader.startNewGame(JSON_DIRECTORY + "KenFourCardGin.json", playerInfoRecords);
    GameMainScene view = new GameMainScene(translator, game, runnerLoader);
    CardViewManager cardManager = new CardViewManager(view, translator, new Firebase());
    game.getBoard().register(cardManager);
    game.step();
    stage.setScene(view.getScene());
    return null;
  }

}
