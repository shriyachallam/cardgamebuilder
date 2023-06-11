package oogasalad.runner.view.player_area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class InactivePlayerArea implements PlayerAreaInterface {

  ArrayList<Node> playerViewList = new ArrayList<>();
  HashMap<Integer, Integer> playerToIndexMap = new HashMap<>();
  VBox pane;

  int firstPlayer;

  public InactivePlayerArea() {
    pane = new VBox(10);
    pane.setId("inactivePlayerArea");
  }

  @Override
  public Pane getNode() {
    return pane;
  }

  @Override
  public void addPlayerView(Node view, int playerId) {
    if (playerViewList.isEmpty()) {
      firstPlayer = playerId;
    }
    playerViewList.add(view);
    playerToIndexMap.put(playerId, playerViewList.size() - 1);
    switchPlayerView(firstPlayer);
  }

  @Override
  public void switchPlayerView(int playerId) {
    int playerIndex = playerToIndexMap.get(playerId);
    if (playerViewList.size() > 1) {
      pane.getChildren().clear();// create a new list with the same elements as cardGroups
      List<Node> rotatedList = new ArrayList<>(playerViewList.size());
      rotatedList.addAll(playerViewList.subList(playerIndex,
          playerViewList.size())); // add the sublist from playerNumber to the end of the list
      rotatedList.addAll(playerViewList.subList(0, playerIndex));
      boolean first = true;
      for (Node playerView : rotatedList.subList(1, playerViewList.size())) {
        if (!first) {
          pane.getChildren().add(new Separator());
        }
        pane.getChildren().add(playerView);
        first = false;
      }
    }
  }

}
