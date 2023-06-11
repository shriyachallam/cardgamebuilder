package oogasalad.runner.view.game_views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.model.board.Board;

public class ActionButtonArea extends VBox {
  private final VBox buttonBox = new VBox();
  Label label;

  public ActionButtonArea(Translator translator) {
    super();
    setId("actionButtonPanel");
    label = new Label(translator.translateToUserSpace("AvailableActions"));
    buttonBox.setId("actionButtonBox");
    getChildren().addAll(label, buttonBox);
  }

  public void clearButtons() {
    buttonBox.getChildren().clear();
  }

  public void addButton(Button button) {
    buttonBox.getChildren().add(button);
  }
}
