package oogasalad.runner.view.player_selection_scene;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.view.game_views.PlayerIcon;
import oogasalad.runner.view.player_view.PlayerIconFactory;
import oogasalad.runner.view.utilities.NodeConfigurer;

/**
 * This class is responsible for displaying all the players that have been created so far.
 */
public class AllPlayersPane{
  private final Pane allPlayerPane;
  private final ScrollPane scrollPane;
  private final PlayerIconFactory playerIconFactory = new PlayerIconFactory();

  public AllPlayersPane(Translator translator){
    Label instruction = new Label();
    instruction.setText(translator.translateToUserSpace("Players"));

    allPlayerPane=NodeConfigurer.configurePane(new VBox(),"allPlayerPane", instruction);
    VBox.setVgrow(allPlayerPane, javafx.scene.layout.Priority.ALWAYS);

    scrollPane= NodeConfigurer.configureScrollPanel("scrollPane", allPlayerPane);
    VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);


  }

  /**
   * Returns the node that contains all the players.
   * @return
   */
  public Node getNode(){
    return scrollPane;
  }

  /**
   * Adds a player to the list of players.
   * @param name
   * @param iconIndex
   */
  public void addPlayer(String name, int iconIndex){
    PlayerIcon icon =playerIconFactory.createPlayerIcon(name, iconIndex);
    icon.getNode().setId("playerIcon");
    allPlayerPane.getChildren().addAll(icon.getNode());
  }

}

