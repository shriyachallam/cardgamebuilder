package oogasalad.runner.view.utilities;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class NodeConfigurer {
  public static ScrollPane configureScrollPanel(String id, Node node){
    ScrollPane pane = new ScrollPane();
    pane.setFitToWidth(true);
    pane.setFitToHeight(true);
    pane.setId(id);
    pane.setContent(node);
    return pane;
  }

  public static Pane configurePane(Pane pane, String id, Node... node){
    pane.setId(id);
    pane.getChildren().addAll(node);
    return pane;
  }

  public static Pane configurePane(Pane pane, String id, List<Node> nodes){
    pane.setId(id);
    pane.getChildren().addAll(nodes);
    return pane;
  }

}
