//package oogasalad.runner.controller;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import javafx.application.Application;
//import javafx.stage.Stage;
//import oogasalad.file_manager.runner_file_manager.runner_loader.DefaultLoader;
//import oogasalad.file_manager.runner_file_manager.runner_loader.RunnerLoader;
//import oogasalad.file_manager.runner_file_manager.runner_loader.records.PlayerInfoRecord;
//import oogasalad.i18n.AppType;
//import oogasalad.i18n.translators.DefaultTranslator;
//import oogasalad.i18n.translators.Translator;
//import oogasalad.runner.model.player.Player;
//import oogasalad.runner.model.stage.Game;
//import oogasalad.runner.view.CardViewManager;
//import oogasalad.runner.view.game_views.GameMainScene;
//import oogasalad.runner.view.player_selection_scene.PlayerSelectionScene;
//
//
//public class TestPlayerSelectionScene extends Application {
//
//  /**
//   * Initializes the scene controller
//   */
//
//  private static final String USER_DIRECTORY = System.getProperty("user.dir");
//  private static final String JSON_DIRECTORY = USER_DIRECTORY + "/data/json/";
//
//
//  private RunnerLoader runnerLoader;
//  private Translator translator;
//
//  public static void main(String[] args) {
//    launch(args);
//  }
//
//  @Override
//  public void start(Stage stage) {
//    PlayerSelectionScene playerSelectionScene = new PlayerSelectionScene();
//    stage.setScene(playerSelectionScene);
//    stage.show();
//  }
//
//}
