package oogasalad.runner.view.game_views;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import oogasalad.file_manager.runner_file_manager.runner_loader.RunnerLoader;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.model.stage.Game;
import oogasalad.runner.view.utilities.NodeConfigurer;

/**
 * This class is the top bar of the game scene. It contains the save button.
 */
public class TopBar {
  private final Node topBar;
  private final Translator translator;
  private final String FILE_PATH = "oogasalad.runner.theme";
  private final ResourceBundle gameProps = ResourceBundle.getBundle(FILE_PATH);
  private final Function<String, Void> themeChange;

  public TopBar(Translator translator, RunnerLoader saver, Game game, Function<String, Void> themeChange) {
    this.translator = translator;
    this.themeChange = themeChange;
    Node comboBox=setUpComboBox();
    Button saveButton = new Button(translator.translateToUserSpace("Save"));
    saveButton.setId("saveButton");
    saveButton.setOnAction(click -> {
      File file = new FileChooser().showSaveDialog(null);
      if (file != null) {
        saver.saveGame(game, file.getPath());
      } else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(translator.translateToUserSpace("Error"));
        alert.setHeaderText(translator.translateToUserSpace("NoFileSelected"));
        alert.showAndWait();
      }
    });

    topBar= NodeConfigurer.configurePane(new HBox(),"topBar", comboBox, saveButton);
  }

  private Node setUpComboBox(){
    List<String> themes= List.of(gameProps.getString("themes").split(","));
    List<String> themesTranslated=themes.stream().map(translator::translateToUserSpace).toList();
    ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(themesTranslated));
    comboBox.setValue(themesTranslated.get(0));
    comboBox.setOnAction(click->{
      String theme=themes.get(themesTranslated.indexOf(comboBox.getValue()));
      String themePath=String.format("/oogasalad/runner/GameScene%s.css",theme);
      themeChange.apply(themePath);
    });
    return comboBox;
  }

  public Node getNode(){
    return topBar;
  }
}
