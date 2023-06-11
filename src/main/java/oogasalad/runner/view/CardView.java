package oogasalad.runner.view;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import oogasalad.runner.model.card.Card;

/**
 * This class is used to represent a card in the game. An ImageView is used to display the card
 * image which can be either face up or face down.
 */
public class CardView{
  boolean faceUp = true;
  BiConsumer<Boolean, Integer> onMouseClicked;
  boolean selected=false;

  String suit;
  int value;
  int id;
  Image cardFront;
  Image cardBack;
  ImageView cardImage;

  public CardView(Image front, Image back, int id, BiConsumer<Boolean, Integer> onMouseClicked, String suit, int value){
    super();
    this.cardFront = front;
    this.cardBack = back;
    this.cardImage = new ImageView(front);
    this.cardImage.setId("card" + suit + value);
    this.suit = suit;
    this.value = value;
    this.id = id;
    this.setOnMouseClicked(onMouseClicked);
    configureImageView();
  }

  private void configureImageView(){
    double aspectRatio = cardImage.getImage().getWidth() / cardImage.getImage().getHeight();
    double maxHeight = 80;
    double newWidth = maxHeight * aspectRatio;
    cardImage.setFitWidth(newWidth);
    cardImage.setFitHeight(maxHeight);
  }

  /** This method sets the function to be called when a card is clicked **/
  public void setOnMouseClicked(BiConsumer<Boolean, Integer> r){
    this.onMouseClicked = r;
    cardImage.setOnMouseClicked(e -> {
      if(e.getClickCount()==1){
        setSelect(!selected);
        r.accept(selected, id);
      }
    });
  }

  /** This method returns a duplicate of the card **/
  public CardView duplicate(){
    CardView newCard=new CardView( cardFront, cardBack, id, onMouseClicked, suit, id);
    newCard.setOnMouseClicked(
        onMouseClicked); //TODO: there has to be a better way to insert this functionality...card factory perhaps??
    return newCard;
  }

  public Node getNode(){
    return cardImage;
  }

  public int getId() {
    return id;
  }

  /**
   * This method sets the face up status of the card
   **/
  //TODO: need to actually change the image to face up or face down by editing the imageview
  public void setFaceUp(boolean faceUp) {
    this.faceUp = faceUp;
    if(faceUp){
      cardImage.setImage(cardFront);
    } else {
      cardImage.setImage(cardBack);
    }
  }

  /**
   * This method sets the border of the card group to indicate that it is selected
   **/
  public void setSelect(boolean isSelected) {
    this.selected = isSelected;
    if(isSelected){
      cardImage.getStyleClass().add("selected-card");
    } else {
      cardImage.getStyleClass().clear();
    }
  }
}
