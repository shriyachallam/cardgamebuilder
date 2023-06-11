package oogasalad.runner.view.card_group;

import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import oogasalad.runner.view.CardView;
/**
 * This class is used to create a card group that is not tied to a specific player (i.e. the deck, discard pile, etc.)
 */
public class StandardCardGroup implements CardGroupView {

  private final int cardGroupId;
  private boolean faceUp = true;
  private final Pane pane;
  private final HashMap<Integer, CardView> cards = new HashMap<>();
  private boolean selected = false;
  private final ImageView emptyGroupIcon;


  /**
   * This constructor creates a new StandardCardGroup object
   *
   * @param cardGroupId the id of the card group
   * @param pane        the pane that the card group will be displayed on
   */
  public StandardCardGroup(int cardGroupId, Pane pane, Image emptyGroup){
    this.cardGroupId = cardGroupId;
    this.pane = pane;
    this.emptyGroupIcon = new ImageView(emptyGroup);
    this.pane.setId("card-group" + cardGroupId);
    this.pane.getStyleClass().add("card-group");
    configureImageView();
  }


  private void configureImageView(){
    double aspectRatio = emptyGroupIcon.getImage().getWidth() / emptyGroupIcon.getImage().getHeight();
    double maxHeight = 80;
    double newWidth = maxHeight * aspectRatio;
    emptyGroupIcon.setFitWidth(newWidth);
    emptyGroupIcon.setFitHeight(maxHeight);
  }

  /**
   * This method sets the behavior of the card group when it is clicked
   */
  @Override
  public void setOnMouseClicked(BiConsumer<Boolean, Integer> r) {
    pane.setOnMouseClicked(e -> {
      if (e.getClickCount() == 2 & !selected) {
        setGroupSelect(true);
        r.accept(true, cardGroupId);
      } else if (e.getClickCount() == 1 & selected) {
        setGroupSelect(false);
        r.accept(false, cardGroupId);
      }
    });
  }

  /**
   * This method adds a card to the group
   *
   * @param card - the card to add
   */
  @Override
  public void addCard(CardView card) {
    if(cards.isEmpty()){
      pane.getChildren().remove(0);
      pane.setBackground(null);
    }
    card.setFaceUp(faceUp);
    cards.put(card.getId(), card);
    pane.getChildren().add(card.getNode());
  }

  /**
   * This method removes a card from the group
   *
   * @param cardId - the id of the card to remove
   */
  @Override
  public void removeCard(int cardId) {
    pane.getChildren().remove(cards.get(cardId).getNode());
    cards.remove(cardId);
    if(cards.isEmpty()){
      pane.getChildren().add(emptyGroupIcon);
    }
  }

  @Override
  public void clear() {
    cards.clear();
    pane.getChildren().clear();
    pane.getChildren().add(emptyGroupIcon);
  }

  /**
 *  * This method sets the face up status of all cards in the group
   * @param isFaceUp - the face up status to set
   */
  @Override
  public void setFaceUp(boolean isFaceUp) {
    faceUp = isFaceUp;
    cards.values().forEach(card -> card.setFaceUp(isFaceUp));
  }
  /**
   * This method sets the select status of all cards in the group
   * @param isSelected - the select status to set
   */
  @Override
  public void setSelect(boolean isSelected) {
    cards.values().forEach(card -> card.setSelect(isSelected));
  }
  /**
   * This method sets the group select status of the group
   * @param isSelected - the group select status to set
   */
  @Override
  public void setGroupSelect(boolean isSelected) {
    selected=isSelected;
    if(isSelected){
      pane.getStyleClass().add("selected-group");
    }
    else{
      pane.getStyleClass().clear();
      pane.getStyleClass().add("card-group");
    }

  }

  @Override
  public boolean isEmpty() {
    return cards.isEmpty();
  }

  /**
   * This method returns the ids of all cards in the group
   * @return the ids of all cards in the group
   */
  @Override
  public Set<CardView> getCardViews(){
    return Set.of(cards.values().toArray(new CardView[0]));
  }
  /**
   * This method returns the id of the card group
   * @return the id of the card group
   */
  public int getId() {
    return cardGroupId;
  }
  /**
   * This method returns the node that the card group is displayed on
   * @return the node that the card group is displayed on
   */
  @Override
  public Node getNode() {
    return pane;
  }
}
