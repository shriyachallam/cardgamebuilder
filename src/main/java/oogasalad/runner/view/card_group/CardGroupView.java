package oogasalad.runner.view.card_group;

import java.util.Set;
import java.util.function.BiConsumer;
import javafx.scene.Node;
import oogasalad.runner.view.CardView;

/**
 * This interface is used to display a group of cards. It is implemented by StandardCardGroup which
 * is a standalone group of cards and player card group which is a group of cards associated with a
 * player and must manage both an active and inactive view.
 */

public interface CardGroupView {

  /**
   * This method adds a card to the group
   *
   * @param card - the card to add
   */
  void addCard(CardView card);

  /**
   * This method removes a card from the group
   *
   * @param cardId - the id of the card to remove
   */
  void removeCard(int cardId);

  void clear();

  /**
   * This method sets the face up status of all cards in the group
   *
   * @param isFaceUp - the face up status to set
   */
  void setFaceUp(boolean isFaceUp);

  void setSelect(boolean isSelected);

  /**
   * This method returns the card ids in the group
   *
   * @return - the card ids in the group
   */

  Set<CardView> getCardViews();

  /**
   * This method returns the card group id
   *
   * @return - the card group id
   */

  int getId();

  /**
   * This method returns the node for display associated with the card group
   *
   * @return - the node associated with the card group
   */
  Node getNode();

  /**
   * This method sets the function to be called when a card is clicked
   *
   * @param r - the function to be called when a card is clicked
   */
  void setOnMouseClicked(BiConsumer<Boolean, Integer> r);

  /**
   * This method sets the border of the card group to indicate that it is selected
   *
   * @param b - true if the border should be selected, false otherwise
   */

  void setGroupSelect(boolean b);

  boolean isEmpty();
}
