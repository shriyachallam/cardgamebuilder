package oogasalad.runner.model.player;

import oogasalad.runner.model.card.group.ImmutableCardGroup;

/**
 * ImmutablePlayer is an interface that represents a player in the game. It cannot be changed by the
 * consumer of the interface.
 */

public interface ImmutablePlayer {

  ImmutableCardGroup hand();

  int id();

  String name();

  double points();

  boolean isWinner();

  boolean isActive();

  int getIconId();
}
