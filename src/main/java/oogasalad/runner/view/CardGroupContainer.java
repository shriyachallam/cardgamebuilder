package oogasalad.runner.view;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import oogasalad.runner.view.card_group.CardGroupView;

public class CardGroupContainer {
  Pane pane;
  Label label;
  ArrayList<CardGroupView> cardGroups = new ArrayList<>();

  public CardGroupContainer(String name, Pane pane) {
    this.label = new Label(name);
    this.pane = pane;
    pane.setId("card-container");
  }

  public Pane getNode() {
    return pane;
  }

  public boolean containsEmptyGroup() {
    int count = 0;
    for (CardGroupView group : cardGroups) {
        if (group.isEmpty()) {
          count ++;
          if(count == 2) return true;
        }
    }
    return false;
  }

  public void addCardGroup(CardGroupView group) {
      if(pane.getChildren().isEmpty()) {
        pane.getChildren().add(label);
      }
      cardGroups.add(group);
      pane.getChildren().add(group.getNode());
  }

  public void clear() {
    pane.getChildren().clear();
    cardGroups.clear();
  }
}
