package oogasalad.splash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oogasalad.builder.frontend.GUIFactory;

import java.util.ResourceBundle;
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

public class RunnerHome extends Scene {
  private final String Splash_CSS = "BuilderUI/CSSFiles/splash.css";
  // private static ResourceBundle runProperties = ResourceBundle.getBundle("BuilderUI/LanguageFiles/English");
  private GUIFactory f;
  //private String language;

  private final String USER_DIRECTORY = System.getProperty("user.dir");
  private final String JSON_DIRECTORY = USER_DIRECTORY + "/data/json/";

  private String filePathToUpload;

  private Stage stage;
  private RunnerLoader runnerLoader;
  private static Translator translator = new DefaultTranslator(AppType.BUILDER, "English");
  private String language;
  private Firebase myFirebase;
  public RunnerHome(BorderPane root, String language, Firebase myFirebase) {
      super(root, Integer.parseInt(translator.translateToUserSpace("RunGamesWidth")),
        Integer.parseInt(translator.translateToUserSpace("RunGamesHeight")));
    this.getStylesheets().add(Splash_CSS);
    this.language = language;
    translator = new DefaultTranslator(AppType.BUILDER, language);
    this.myFirebase = myFirebase;

    f = new GUIFactory("RunHome");

    Label runHomeTitle = f.createLabel(translator.translateToUserSpace("RunGamesTitle"),
        "runHomeTitle", Pos.CENTER);
    root.setTop(runHomeTitle);
    root.setAlignment(runHomeTitle, Pos.CENTER);

    FileChooser picker = new FileChooser();
    picker.setInitialDirectory(new File(JSON_DIRECTORY));
    Button pickNewButton = f.createButton(translator.translateToUserSpace("YourGame"), "UploadNew",
        Pos.CENTER);
    pickNewButton.setOnAction(event -> {
      try {
        filePathToUpload = picker.showOpenDialog(this.getWindow()).getPath();
        launchNewGame();
      } catch (NullPointerException e) {
        filePathToUpload = null;
      }

    });

    Button pickResumeButton = f.createButton(translator.translateToUserSpace("ResumeGame"),
        "UploadResume", Pos.CENTER);
    pickResumeButton.setOnAction(event -> {
      try {
        filePathToUpload = picker.showOpenDialog(this.getWindow()).getPath();
        launchSavedGame();
      } catch (NullPointerException e) {
        filePathToUpload = null;
      }

    });

    VBox gameOnUI = f.formatVBox(new VBox(pickNewButton, pickResumeButton), 20, Pos.CENTER);
    populateFromFolder(gameOnUI, JSON_DIRECTORY + "games/final_game_templates/");
    populateFromFolder(gameOnUI, JSON_DIRECTORY + "games/firebase/");
    root.setCenter(gameOnUI);
  }


  /**
   * Populates the given vbox with buttons for each file in the given directory Each game file in
   * the directory should be formatted in camel case with no spaces The button text will be the file
   * name with spaces added between each word "MyGame.json" will become "My Game"
   *
   * @param vbox
   * @param directory
   * @Author: Ken Kalin
   */
  private void populateFromFolder(VBox vbox, String directory) {
    File folder = new File(directory);
    File[] files = folder.listFiles();
    for (File file : files) {
      if (file.isFile() && !file.getName().equalsIgnoreCase("README.md")) {
        String fileName = file.getName();

        String buttonName = file.getName().replaceAll("\\.json", "");
        buttonName = buttonName.replaceAll("([a-z])([A-Z])",
            "$1 $2");//Anytime there is a lowercase letter followed by an uppercase letter, add a space between them

        Button fileButton = new Button(buttonName);

        fileButton.setOnAction(event -> {
          filePathToUpload = directory + fileName;
          launchNewGame();
        });
        vbox.getChildren().add(fileButton);
      }
    }
  }


  private void launchNewGame() {
    stage = new Stage();
    runnerLoader = new DefaultLoader(language);
    translator = new DefaultTranslator(AppType.RUNNER, language);
    PlayerSelectionScene playerSelectionScene = new PlayerSelectionScene(translator,
        this::beginGame);
    stage.setScene(playerSelectionScene);
    stage.show();
  }

  private void launchSavedGame() {
    stage = new Stage();
    runnerLoader = new DefaultLoader(language);
    translator = new DefaultTranslator(AppType.RUNNER, language);
    Game game = runnerLoader.loadExistingGame(filePathToUpload);
    GameMainScene view = new GameMainScene(translator, game, runnerLoader);
    CardViewManager cardManager = new CardViewManager(view, translator, myFirebase);
    game.getBoard().register(cardManager);
    game.step();
    stage.setScene(view.getScene());
    stage.show();
  }

  private Void beginGame(List<PlayerInfoRecord> playerInfoRecords) {
    Game game = runnerLoader.startNewGame(filePathToUpload, playerInfoRecords);
    GameMainScene view = new GameMainScene(translator, game, runnerLoader);
    CardViewManager cardManager = new CardViewManager(view, translator, myFirebase);
    game.getBoard().register(cardManager);
    game.step();
    stage.setScene(view.getScene());
    return null;
  }
}
