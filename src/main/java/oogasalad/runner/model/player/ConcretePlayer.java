package oogasalad.runner.model.player;

import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.card.group.CardGroup.Direction;
import oogasalad.runner.model.card.group.CardGroup.ViewType;
import oogasalad.runner.model.card.group.ConcreteCardGroup;

/**
 * implementation of Player
 */
public class ConcretePlayer implements Player {

  private final String name;

  private final CardGroup hand;
  private boolean isWinner;
  private int points;
  private boolean isActive;

  private int id;
  private int iconId;

  public ConcretePlayer(String name) {
    points = 0;
    this.name = name;
    hand = new ConcreteCardGroup("hand", ViewType.SEQUENCE, Direction.FACE_UP);
    this.id = this.hashCode();
  }

  public ConcretePlayer(String name, int iconId) {
    points = 0;
    this.name = name;
    hand = new ConcreteCardGroup("hand", ViewType.SEQUENCE, Direction.FACE_UP);
    this.id = this.hashCode();
    this.iconId = iconId;
  }
  @Override
  public int id() {
    return id;
  }
  @Override
  public CardGroup hand() {
    return this.hand;
  }
  @Override
  public String name() {
    return name;
  }
  @Override
  public double points() {
    return points;
  }
  @Override
  public void addPoints(double points) {
    this.points += points;
  }
  @Override
  public boolean isWinner() {
    return isWinner;
  }
  @Override
  public void setWinner(boolean isWinner) {
    this.isWinner = isWinner;
  }
  @Override
  public int getPoints() {
    return points;
  }
  @Override
  public boolean isActive() {
    return isActive;
  }

  @Override
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public int getIconId() {
    return iconId;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Player other) {
      return other.id() == this.id();
    }
    return false;
  }
}
