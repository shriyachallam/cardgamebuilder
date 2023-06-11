package oogasalad.runner.view.player_selection_scene;

import java.util.List;
import java.util.function.BiFunction;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.view.game_views.PlayerIcon;
import oogasalad.runner.view.player_view.PlayerIconFactory;
import oogasalad.runner.view.utilities.NodeConfigurer;

/**
 * This class allows the user to create a new player.
 */
public class CreatePlayerPane{
  private final Pane makePlayerPane;
  private final ImageView selectedPlayerIcon;
  private Pane selectableIcons=new HBox();
  private final TextField playerNameField = new TextField();
  private final Image defaultPlayerIcon;
  private int selectedPlayerIconIndex = 0;
  private final PlayerIconFactory playerIconFactory = new PlayerIconFactory();
  private final BiFunction<String, Integer, Void> createPlayerCallback;

  public CreatePlayerPane(Translator translator, BiFunction<String, Integer, Void> createPlayerCallback){
    this.createPlayerCallback = createPlayerCallback;
    setUpSelectableIcons();

    Label instruction = new Label();
    instruction.setText(translator.translateToUserSpace("CreatePlayer"));

    defaultPlayerIcon = playerIconFactory.createPlayerIcon(selectedPlayerIconIndex).getImage();

    selectedPlayerIcon=new ImageView(defaultPlayerIcon);
    selectedPlayerIcon.setId("selectedPlayerIcon");
    playerNameField.setId("playerNameField");
    Button createPlayerButton=new Button(translator.translateToUserSpace("Create"));
    createPlayerButton.setId("createPlayerButton");
    createPlayerButton.setOnMouseClicked(e -> {
      handlePlayerCreation();
    });

    Pane nameBox = NodeConfigurer.configurePane(new HBox(), "nameBox", new Label(translator.translateToUserSpace("Name")), playerNameField, createPlayerButton);
    makePlayerPane= NodeConfigurer.configurePane(new VBox(), "makePlayerPane", instruction, selectedPlayerIcon, selectableIcons, nameBox);
  }

  private void handlePlayerCreation(){
    createPlayerCallback.apply(playerNameField.getText(), selectedPlayerIconIndex);
    playerNameField.clear();
    selectedPlayerIcon.setImage(defaultPlayerIcon);
    selectedPlayerIconIndex = 0;
  }
  private void setUpSelectableIcons(){
    List<PlayerIcon> playerIcons = playerIconFactory.getAllPlayerIcons();
    for (int i = 0; i < playerIcons.size(); i++) {
      PlayerIcon icon = playerIcons.get(i);
      icon.getNode().setId("selectableIcon"+i);
      int finalI = i;
      icon.getNode().setOnMouseClicked(e -> {
        selectedPlayerIcon.setImage(icon.getImage());
        selectedPlayerIconIndex = finalI;
      });
    }
    selectableIcons= NodeConfigurer.configurePane(new HBox(),"selectableIcons",playerIcons.stream().map(PlayerIcon::getNode).toList());
    selectableIcons.getChildren().addAll();
  }

  /**
   * Returns the node that contains the player creation pane.
   * @return
   */
  public Node getNode(){
    return makePlayerPane;
  }

}
