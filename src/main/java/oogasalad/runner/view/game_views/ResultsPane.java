package oogasalad.runner.view.game_views;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import oogasalad.i18n.TranslationPackage;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.view.utilities.NodeConfigurer;

public class ResultsPane {
  private final Pane pane;
  private final Label label = new Label();
  private final Translator translator;

  /**
   * This class displays the results of the game after it is over.
   */
  public ResultsPane(Translator translator){
    this.translator = translator;
    pane=NodeConfigurer.configurePane(new VBox(), "playerTransitionPane", label);
    pane.setVisible(false);
  }

  public void show(String name){
    TranslationPackage translationPackage = new TranslationPackage("GameOver", name);
    label.setText(translator.translateToUserSpace(translationPackage));
    pane.setVisible(true);
  }
  public Pane getNode(){
    return pane;
  }

}
