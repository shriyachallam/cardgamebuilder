package oogasalad.runner.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import oogasalad.util.DukeApplicationTest;
import org.junit.jupiter.api.Test;

public class CardViewTest extends DukeApplicationTest {
  private CardView cardView;
  private ImageView cardImageMock = new ImageView();

  @Override
  public void start(Stage primaryStage){
    Image cardFront = new Image("oogasalad/runner/cardIcons/cardImages/2_of_clubs.png");
    Image cardBack = new Image("oogasalad/runner/cardIcons/cardImages/2_of_clubs.png");
    int id = 1;
    cardView = new CardView(cardFront, cardBack, id, (event, cardView) -> {}, "clubs", 2);
    cardView.cardImage = cardImageMock;
    primaryStage.setScene(new Scene(new Pane(cardView.getNode())));
    primaryStage.show();
  }

  @Test
  public void testSetSelect() {
    cardView.setSelect(true);
    assertTrue(cardView.cardImage.getStyleClass().contains("selected-card"));
  }

  @Test
  public void testDuplicate() {
    CardView duplicateCardView = cardView.duplicate();

    assertEquals(cardView.cardFront, duplicateCardView.cardFront);
    assertEquals(cardView.cardBack, duplicateCardView.cardBack);
    assertEquals(cardView.id, duplicateCardView.id);
    assertEquals(cardView.onMouseClicked, duplicateCardView.onMouseClicked);
  }

  @Test
  public void testGetNode() {
    Node node = cardView.getNode();
    assertEquals(cardView.cardImage, node);
  }

  @Test
  public void testGetId() {
    int id = cardView.getId();
    assertEquals(cardView.id, id);
  }
}