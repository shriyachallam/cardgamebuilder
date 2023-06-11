package oogasalad.runner.model.player;

import oogasalad.runner.model.card.group.CardGroup;

/**
 * Player is an interface that represents a player in the game. It has methods to change the state
 * of the player.
 */

public interface Player extends ImmutablePlayer {

  void addPoints(double points);
  int getPoints();

  CardGroup hand();

  void setWinner(boolean isWinner);

  void setActive(boolean isActive);
  int getIconId();

}
