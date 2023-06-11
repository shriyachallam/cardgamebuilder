package oogasalad.runner.view.game_views;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import oogasalad.i18n.TranslationPackage;
import oogasalad.i18n.translators.Translator;

public class PlayerTransitionPane {
  private final Pane pane;
  private final Translator translator;
  private final Button button = new Button();
  public PlayerTransitionPane(Translator translator){
    pane = new VBox();
    this.translator = translator;
    pane.setId("playerTransitionPane");
    pane.setVisible(false);
    button.setOnAction(e -> {
      pane.setVisible(false);
    });
    button.setId("playerTransitionButton");
    pane.getChildren().addAll(button);
  }

  public void show(String name){
    TranslationPackage translationPackage = new TranslationPackage("SwitchTo", name);
    button.setText(translator.translateToUserSpace(translationPackage));
    pane.setVisible(true);
  }
  public Pane getNode(){
    return pane;
  }

}
