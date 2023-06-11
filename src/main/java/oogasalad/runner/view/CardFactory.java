package oogasalad.runner.view;

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import javafx.scene.image.Image;
import oogasalad.Main;

/**
 * This class is used to create a card view.
 */
//TODO: combine this class with the CardImageResource class
public class CardFactory {
  private final String FILE_PATH = "oogasalad.runner.cardIcons.CardIcons";
  private final ResourceBundle cardProps = ResourceBundle.getBundle(FILE_PATH);
  private final BiConsumer<Boolean, Integer> setOnMouseClicked;
  public CardFactory(BiConsumer<Boolean, Integer> setOnMouseClicked) {
    this.setOnMouseClicked = setOnMouseClicked;
  }

  public CardView createCardView(String suit, int value, int id) {
    Image imageFront = getCardFrontImage(value, suit);
    Image imageBack = getCardBackImage();
    return new CardView(imageFront, imageBack, id, setOnMouseClicked, suit, value);
  }

  public Image getCardFrontImage(int cardNumber, String cardType){
    String fileName=cardProps.getString("%d%s".formatted(cardNumber, cardType));
    return new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/oogasalad/runner/cardIcons/cardImages/%s".formatted(fileName))));
  }

  public Image getCardBackImage(){
    String fileName=cardProps.getString("back");
    return new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/oogasalad/runner/cardIcons/cardImages/%s".formatted(fileName))));
  }

}
