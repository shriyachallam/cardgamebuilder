package oogasalad.runner.view.player_selection_scene;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import oogasalad.file_manager.runner_file_manager.runner_loader.records.PlayerInfoRecord;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.view.utilities.NodeConfigurer;

public class PlayerSelectionScene extends Scene {
  private final List<PlayerInfoRecord> playerInfoRecords=new ArrayList<>();
  private final AllPlayersPane allPlayerPane;
  private final Translator translator;
  private DialogPane dialogPane;

  public PlayerSelectionScene(Translator translator, Function<List<PlayerInfoRecord>, Void> continueCallback) {
    super(new HBox());
    this.translator = translator;
    this.getStylesheets().add("/oogasalad/runner/PlayerSelectionScene.css");

    allPlayerPane = new AllPlayersPane(translator);

    CreatePlayerPane createPlayerPane = new CreatePlayerPane(translator, this::addPlayer);

    Button continueButton = new Button(translator.translateToUserSpace("Continue"));
    continueButton.setId("ContinueButton");
    continueButton.setOnMouseClicked(e -> {
      if(playerInfoRecords.isEmpty()){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(translator.translateToUserSpace("Error"));
        alert.setHeaderText(translator.translateToUserSpace("NoPlayer"));
        dialogPane = alert.getDialogPane();
        dialogPane.setId("noPlayerError");
        alert.showAndWait();
        return;
      }
      continueCallback.apply(playerInfoRecords);
    });

    Pane sidePane = NodeConfigurer.configurePane(new VBox(), "sidePane", allPlayerPane.getNode(), continueButton);
    NodeConfigurer.configurePane((HBox) getRoot(), "mainContainer", createPlayerPane.getNode(), sidePane);
  }


  /**
   * This method adds a player to the list of players and updates the display.
   **/

  private Void addPlayer(String name, int iconIndex) {
    if (name.isBlank()) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle(translator.translateToUserSpace("Error"));
      alert.setHeaderText(translator.translateToUserSpace("NoName"));
      dialogPane = alert.getDialogPane();
      dialogPane.setId("noNameError");
      alert.showAndWait();
      return null;
    }
    if (playerInfoRecords.stream().anyMatch(record -> record.name().equals(name))) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle(translator.translateToUserSpace("Error"));
      alert.setHeaderText(translator.translateToUserSpace("DuplicateName"));
      dialogPane = alert.getDialogPane();
      dialogPane.setId("duplicateNameError");
      alert.showAndWait();
      return null;
    }
    playerInfoRecords.add(new PlayerInfoRecord(name, iconIndex));
    allPlayerPane.addPlayer(name, iconIndex);
    return null;
  }

}
