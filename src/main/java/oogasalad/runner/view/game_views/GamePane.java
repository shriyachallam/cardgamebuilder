package oogasalad.runner.view.game_views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.model.board.ImmutableBoard;
import oogasalad.runner.view.player_area.ActivePlayerArea;
import oogasalad.runner.view.player_area.InactivePlayerArea;
import oogasalad.runner.view.utilities.NodeConfigurer;

/**
 * This class contains the GUI elements for the game screen
 */
public class GamePane {
  BorderPane gamePane=new BorderPane();
  Pane boardView;
  private final InactivePlayerArea opponentArea = new InactivePlayerArea();
  private final ActivePlayerArea playerArea = new ActivePlayerArea();
  private ActionButtonArea actionButtonArea;
  private final Translator translator;
  ImmutableBoard immutableBoard;
  public GamePane(Translator translator, ImmutableBoard immutableBoard) {
    this.translator = translator;

    this.immutableBoard = immutableBoard;
    Node leftPanel= createLeftPanel();
    Node rightPanel = createRightPanel();
    Node middlePanel = createMiddlePanel();

    gamePane.setLeft(leftPanel);
    gamePane.setRight(rightPanel);
    gamePane.setCenter(middlePanel);
  }

  private Node createLeftPanel(){
    return NodeConfigurer.configureScrollPanel("inactivePlayerView", opponentArea.getNode());
  }

  private Node createRightPanel(){
    actionButtonArea = new ActionButtonArea(translator);
    ScrollPane actionPanel=NodeConfigurer.configureScrollPanel("actionPanel", actionButtonArea);
    Node assistPane = new AssistPane(translator,immutableBoard).getNode();
    return NodeConfigurer.configurePane(new VBox(),"rightPane", actionPanel, assistPane);
  }

  private Node createMiddlePanel(){
    boardView= NodeConfigurer.configurePane(new HBox(),"boardPane");
    VBox.setVgrow(boardView, javafx.scene.layout.Priority.ALWAYS);
    ScrollPane boardPaneScroll = NodeConfigurer.configureScrollPanel("boardPaneScroll", boardView);
    VBox.setVgrow(boardPaneScroll, javafx.scene.layout.Priority.ALWAYS);
    return NodeConfigurer.configurePane(new VBox(),"gamePanel", boardView, playerArea.getNode());
  }

  /**
   * Adds a button to the action button area
   * @param button
   */
  public void addActionButton(Button button) {
    actionButtonArea.addButton(button);
  }

  /**
   * Adds an inactive player view to the game pane
   * @param playerView
   * @param playerId
   */
  public void addInactivePlayerView(Node playerView, int playerId){
    opponentArea.addPlayerView(playerView, playerId);
  }

  /**
   * Adds an active player view to the game pane
   * @param playerView
   * @param playerId
   */
  public void addActivePlayerView(Node playerView, int playerId){
    playerArea.addPlayerView(playerView, playerId);
  }

  /**
   * Switches the active player view to the view of the player with the given id
   * @param newPlayerId
   */
  public void switchPlayerView(int newPlayerId) {
    playerArea.switchPlayerView(newPlayerId);
    opponentArea.switchPlayerView(newPlayerId);
  }


  /**
   * Clears the action button area
   */
  public void clearActionButtons() {
    actionButtonArea.clearButtons();
  }

  /**
   * Adds a card area to the game pane
   * @param node
   */
  public void addCardArea(Pane node) {
    boardView.getChildren().add(node);
  }

  /**
   * Returns the game pane
   * @return
   */
  public Node getNode(){
    return gamePane;
  }

}
