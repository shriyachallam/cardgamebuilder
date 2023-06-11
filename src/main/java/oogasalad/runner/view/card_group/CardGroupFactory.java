package oogasalad.runner.view.card_group;

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import oogasalad.Main;

/**
 * This class is used to create various types of card groups. By using this class, we can create card groups without having to pass
 * in the setOnMouseClicked method everytime we create a new card group.
 */
public class CardGroupFactory {
  private final String FILE_PATH = "oogasalad.runner.gameIcons.gameIcons";
  private final ResourceBundle gameProps = ResourceBundle.getBundle(FILE_PATH);
  private final Image emptySet;

  private final BiConsumer<Boolean, Integer> setOnMouseClicked;

  public CardGroupFactory(BiConsumer<Boolean, Integer> setOnMouseClicked) {
    String fileName=gameProps.getString("add_set");
    emptySet=new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/oogasalad/runner/gameIcons/%s".formatted(fileName))));
    this.setOnMouseClicked = setOnMouseClicked;
  }

  /**
   * This method creates a card group that is a sequence of cards (laid out horizontally)
   * @param id - the id of the card group
   * @return - the card group
   */
  public StandardCardGroup createCardSequence(int id) {

    HBox hbox = new HBox(-30);
    hbox.setMaxWidth(Region.USE_PREF_SIZE);
    StandardCardGroup cardGroup = new StandardCardGroup(id, hbox, emptySet);
    cardGroup.setOnMouseClicked(setOnMouseClicked);
    return cardGroup;
  }

  /**
   * This method creates a card group that is a stack of cards
   * @param id - the id of the card group
   * @return - the card group
   */
  public StandardCardGroup createCardStack(int id) {
    StandardCardGroup cardGroup = new StandardCardGroup(id, new StackPane(), emptySet);
    cardGroup.setOnMouseClicked(setOnMouseClicked);
    return cardGroup;
  }
}

