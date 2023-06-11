package oogasalad.runner.view.game_views;


import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import oogasalad.file_manager.runner_file_manager.runner_loader.RunnerLoader;
import oogasalad.runner.model.card.group.CardGroup.Direction;
import oogasalad.runner.model.stage.Game;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.model.player.ImmutablePlayer;
import oogasalad.runner.view.CardGroupContainer;
import oogasalad.runner.view.card_group.CardGroupView;
import oogasalad.runner.view.utilities.NodeConfigurer;

/**
 * This class is the main scene for the game. It contains the game pane, the player transition pane,
 * and the result pane. It also contains the top bar, which contains the save button and the
 * player's name. It switches between the three panes depending on the state of the game.
 */
public class GameMainScene{
  private final GamePane gamePane;
  private final PlayerTransitionPane playerTransitionPane;
  private final ResultsPane resultsPane;
  private final HashMap<Integer, CardGroupContainer> cardAreaMap = new HashMap<>();
  private int activePlayerId;
  private final Pane mainContainer;
  private final Translator translator;

  public GameMainScene(Translator translator, Game game, RunnerLoader saver) {
    this.translator = translator;

    TopBar topBar = new TopBar(translator, saver, game, this::setTheme);

    playerTransitionPane = new PlayerTransitionPane(translator);
    gamePane = new GamePane(translator,game.getImmutableBoard());
    resultsPane = new ResultsPane(translator);

    Node sceneLayers=NodeConfigurer.configurePane(new StackPane(), "sceneLayers",gamePane.getNode(), playerTransitionPane.getNode(), resultsPane.getNode());
    VBox.setVgrow(sceneLayers, javafx.scene.layout.Priority.ALWAYS);

    mainContainer = NodeConfigurer.configurePane(new VBox(),"mainContainer", topBar.getNode(), sceneLayers);
    mainContainer.getStylesheets().add("/oogasalad/runner/GameSceneGreen.css");
  }

  public Void setTheme(String themePath){
    mainContainer.getStylesheets().clear();
    mainContainer.getStylesheets().add(themePath);
    return null;
  }

  /**
   * Returns the scene of the game.
   * @return the scene of the game
   */
  public Scene getScene() {
    return new Scene(mainContainer);
  }

  /**
   * Adds an inactive player view to the game pane.
   * @param playerView the player view to be added
   * @param playerId the id of the player
   */
  public void addInactivePlayerView(Node playerView, int playerId){
    gamePane.addInactivePlayerView(playerView, playerId);
  }

  /**
   * Adds an active player view to the game pane.
   * @param playerView the player view to be added
   * @param playerId the id of the player
   */
  public void addActivePlayerView(Node playerView, int playerId){
    gamePane.addActivePlayerView(playerView, playerId);
  }

  /**
   * Adds a card area to the game pane.
   * @param areaId the id of the card area
   * @param areaName the name of the card area
   */
  public void addCardArea(int areaId, String areaName){
    String areaNameTranslated;
    try {
      areaNameTranslated = translator.translateToUserSpace(areaName);
    } catch (Exception e) {
      areaNameTranslated = areaName;
    }
    CardGroupContainer cardArea = new CardGroupContainer(areaNameTranslated, new VBox(10));
    gamePane.addCardArea(cardArea.getNode());
    cardAreaMap.put(areaId, cardArea);
  }

  /**
   * Adds an action button to the game pane.
   * @param button the button to be added
   */
  public void addActionButton(Button button) {
    gamePane.addActionButton(button);
  }

  /**
   * Switches the player view to the player with the given id.
   * @param player the player to switch to
   */
  public void switchPlayerView(ImmutablePlayer player) {
    int newPlayerId = player.id();
    String playerName = player.name();
    if(newPlayerId == activePlayerId) {
      return;
    }
    playerTransitionPane.show(playerName);
    gamePane.switchPlayerView(newPlayerId);

    activePlayerId = newPlayerId;
  }

  /**
   * Adds a card group to the card area with the given id.
   * @param cardGroupView the card group to be added
   * @param index the id of the card area
   */
  public void addCardGroupToArea(CardGroupView cardGroupView, int index, Direction direction) {
    if(direction == Direction.FACE_DOWN) {
      cardGroupView.setFaceUp(false);
    }
    cardAreaMap.get(index).addCardGroup(cardGroupView);
  }

  /**
   * Clears all the card areas.
   */
  public void clearCardAreas() {
    cardAreaMap.values().forEach(CardGroupContainer::clear);
  }

  /**
   * Clears all the action buttons.
   */
  public void clearActionButtons() {
    gamePane.clearActionButtons();
  }

  /**
   * Shows the results pane with the given name.
   * @param name the name of the winner
   */
  public void showWinner(String name){
    resultsPane.show(name);
  }
}
