package oogasalad.runner.view.card_group;

import java.util.Set;
import java.util.function.BiConsumer;
import javafx.scene.Node;
import oogasalad.runner.view.CardView;

/**
 * This object maintains both the cards in a group as an opponent sees them and as the player sees
 * them. As the players turn changes, which view is exposed * will be changed
 * <p>
 * <p>
 * /** This object maintains both the cards in a group as an opponent sees them and as the player
 * sees them. As the players turn changes, which view is exposed will be changed
 */

public class PlayerHandView implements CardGroupView {

  private final CardGroupView playerHandView;
  private final int cardGroupId;
  private final CardGroupView opponentHandView;

  /**
   * This constructor creates a new PlayerCardGroup object
   *
   * @param activePlayerGroup   the card group that the player sees
   * @param inactivePlayerGroup the card group that the opponent sees
   * @param cardGroupId         the id of the card group
   */
  public PlayerHandView(CardGroupView activePlayerGroup, CardGroupView inactivePlayerGroup,
      int cardGroupId) {
    this.playerHandView = activePlayerGroup;
    this.opponentHandView = inactivePlayerGroup;
    this.cardGroupId = cardGroupId;
  }

  /**
   * This method created a duplicate of the cardview object and adds it to both the active and
   * inactive player groups
   *
   * @param card the card to be added
   */
  public void addCard(CardView card) {
    playerHandView.addCard(card);
    CardView cardCopy = card.duplicate();
    opponentHandView.addCard(cardCopy);
  }

  /**
   * This method removes a card from both the active and inactive player groups
   *
   * @param cardId the id of the card to be removed
   */
  @Override
  public void removeCard(int cardId) {
    playerHandView.removeCard(cardId);
    opponentHandView.removeCard(cardId);
  }

  @Override
  public void clear() {
    playerHandView.clear();
    opponentHandView.clear();
  }

  public void setFaceUp(boolean isFaceUp) {
  }

  /**
   * This method returns the card group id
   *
   * @return the card group id
   */
  @Override
  public int getId() {
    return cardGroupId;
  }

  @Override
  public Node getNode() {
    return null;
  }

  /**
   * This method returns the active player group
   *
   * @return the active player group
   */

  public CardGroupView getPlayerHandNode() {
    return playerHandView;
  }

  /**
   * This method returns the inactive player group
   *
   * @return the inactive player group
   */

  public CardGroupView getOpponentHandNode() {
    return opponentHandView;
  }

  @Override
  public void setOnMouseClicked(BiConsumer<Boolean, Integer> r) {
    playerHandView.setOnMouseClicked(r);
    opponentHandView.setOnMouseClicked(r);
  }

  @Override
  public void setGroupSelect(boolean isSelected) {
    playerHandView.setGroupSelect(isSelected);
    opponentHandView.setGroupSelect(isSelected);
  }

  @Override
  public boolean isEmpty() {
    return playerHandView.isEmpty();
  }

  /**
   * This method selects every card in the group
   */
  @Override
  public void setSelect(boolean isSelected) {
    playerHandView.setSelect(isSelected);
    opponentHandView.setSelect(isSelected);
  }

  /**
   * This method returns the card ids in the group
   */
  @Override
  public Set<CardView> getCardViews() {
    return this.playerHandView.getCardViews();
  }

}
