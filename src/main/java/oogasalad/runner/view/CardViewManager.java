package oogasalad.runner.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import oogasalad.file_manager.runner_file_manager.runner_loader.DefaultLoader;
import oogasalad.i18n.TranslationPackage;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.controller.observer.BoardObserver;
import oogasalad.runner.model.board.PlayerBoard;
import oogasalad.runner.model.card.ImmutableCard;
import oogasalad.runner.model.card.group.ImmutableCardGroup;
import oogasalad.runner.model.condition.Condition;
import oogasalad.runner.model.player.ImmutablePlayer;
import oogasalad.runner.view.card_group.CardGroupFactory;
import oogasalad.runner.view.card_group.CardGroupView;
import oogasalad.runner.view.card_group.PlayerHandView;
import oogasalad.runner.view.game_views.GameMainScene;
import oogasalad.runner.view.player_view.PlayerView;
import oogasalad.service.Firebase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CardViewManager implements BoardObserver {

  private final Map<ImmutableCard, CardView> cardMap = new HashMap<>();
  private final Map<ImmutableCardGroup, CardGroupView> cardGroupMap = new HashMap<>();
  private final Map<ImmutablePlayer, PlayerView> playerMap = new HashMap<>();
  private final CardFactory cardFactory = new CardFactory(this::selectSelectable);
  private final CardGroupFactory cardGroupFactory = new CardGroupFactory(this::selectSelectable);
  private final List<String> cardAreas = new ArrayList<>();
  private final GameMainScene gameMainScene;
  private final Translator translator;
  private PlayerBoard board;
  private Firebase myFirebase;

  private static final Logger LOG = LogManager.getLogger(CardViewManager.class);

  /**
   * Updated constructor that takes in a preconfigured translator object
   * @param view configured game scene
   * @param translator configured translator
   */
  public CardViewManager(GameMainScene view, Translator translator, Firebase myFirebase){
    gameMainScene = view;
    this.translator = translator;
    this.myFirebase = myFirebase;
  }

  @Override
  public void linkBoard(PlayerBoard board) {
    this.board = board;
  }

  @Override
  public Runnable getBoardCallback() {
    return () -> {
      if (board == null) {
        return;
      }
      updatePlayers();
      updateCardAreas();
      updateCardGroups();
      updateCards();
      updateButtons();
    };
  }

  private void updatePlayers() {
    board.players().forEach(player -> {
      if (!playerMap.containsKey(player)) {
        playerMap.put(player, createPlayerView(player));
        cardGroupMap.put(player.hand(), createHandView(player.hand()));
        playerMap.get(player).addToPlayerHand((PlayerHandView) cardGroupMap.get(player.hand()));
      }
      cardGroupMap.get(player.hand()).clear();
      if (player.isActive()) {
        gameMainScene.switchPlayerView(player);
      }
      if (player.isWinner()) {
        //myFirebase.updatePlayerStats(board.players());
        gameMainScene.showWinner(player.name());
      }
    });
  }

  private void updateCardGroups() {
    board.groups()
        .stream().filter(group -> !group.tag().equals("hand"))
        .forEach(group -> {
      if (!cardGroupMap.containsKey(group)) {
        cardGroupMap.put(group, createGroupView(group));
      }
      CardGroupView cardGroupView = cardGroupMap.get(group);
      gameMainScene.addCardGroupToArea(cardGroupView, cardAreas.indexOf(group.tag()), group.direction());
      cardGroupView.clear();
      cardGroupView.setGroupSelect(group.isSelected());
    });
  }

  private void updateCards() {
    board.groups().forEach(group -> {
      group.forEach(card -> {
        if (!cardMap.containsKey(card)) {
          cardMap.put(card, createCardView(card));
        }
        cardMap.get(card).setSelect(card.isSelected());
        cardGroupMap.get(group).addCard(cardMap.get(card));
      });
    });
  }

  private void updateCardAreas() {
    gameMainScene.clearCardAreas();
    board.groups().forEach(group -> {
      if (!cardAreas.contains(group.tag())) {
        gameMainScene.addCardArea(cardAreas.size(), group.tag());
        cardAreas.add(group.tag());
      }
    });
  }

  private void updateButtons() {
    gameMainScene.clearActionButtons();
    board.allowedMoves().forEach(action -> {
      Button button = new Button(translator.translateToUserSpace(action.description()));
      button.setOnAction(e ->

          {

            try {
              action.act();
            }
            catch (Exception e1) {
                LOG.error(translator.translateToUserSpace( new TranslationPackage("PlayerActionError",e1.getMessage())));
                board.revertState();//If action crashes, restart phase
            }

          }
      );
      gameMainScene.addActionButton(button);
    });
    Button endPhase = new Button(translator.translateToUserSpace("EndPhase"));
    endPhase.setOnAction(e -> {

    boolean checkAllConditions = true; // set this to true to check all conditions, or false to check at least one condition


      //Check if OrFlagCondition is present in this phase
      //If so, switch checkAllConditions mode to false
      for (Condition condition : board.phaseConditions()) {
        if(condition.description().equals(new TranslationPackage("OrFlag"))) {
              checkAllConditions = false;
            //System.out.println("OrFlag found");
            }
          }

      boolean fail = false;
      int validConditionCount = 0;
      int conditions = 0;
      for (Condition condition : board.phaseConditions()) {
        conditions++;
        if (condition.isValid()) {
          validConditionCount++;
        } else if (checkAllConditions) {
          String errorMessage = translator.translateToUserSpace("PhaseFail") +
              " " + translator.translateToUserSpace(condition.description());
          LOG.error(errorMessage);
          fail = true;
          Alert alert = new Alert(AlertType.ERROR);
          alert.setTitle(translator.translateToUserSpace("Error"));
          alert.setHeaderText(errorMessage);
          alert.showAndWait();
        }
      }


      if(!checkAllConditions && validConditionCount == 0 && conditions > 0) {
        fail = true;
      }
      /*if (!checkAllConditions && validConditionCount == 0) {
        String errorMessage = translator.translateToUserSpace("PhaseFail") +
            " " + translator.translateToUserSpace(condition.description());
        LOG.error(errorMessage);
        fail = true;
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(translator.translateToUserSpace("Error"));
        alert.setHeaderText(errorMessage);
        alert.showAndWait();
      }*/

      if (fail) {
        //TODO throw error here? !!!ERROR POPUPS ON VIEWER WHEN CONDITION FAILS!!!
        // ...
      }







      if (fail) {
        board.revertState();
      } else {
        board.endPhase();
      }
    });
    gameMainScene.addActionButton(endPhase);
  }


  /**
   * This method adds a card sequence to a player's hand
   */
  private PlayerHandView createHandView(ImmutableCardGroup playerHand) {
    CardGroupView playerHandView = cardGroupFactory.createCardSequence(playerHand.id());
    CardGroupView opponentHandView = cardGroupFactory.createCardSequence(playerHand.id());
    opponentHandView.setFaceUp(false);
    return new PlayerHandView(playerHandView, opponentHandView, playerHand.id());
  }

  /**
   * This method is passed to the card group factory and card factory and is the callback for when a card group/card is
   * selected
   */

  private void selectSelectable(boolean select, Integer selectableId) {
    if (select) {
      board.select(selectableId);
    } else {
      board.deselect(selectableId);
    }
  }

  /**
   * This method adds a player to the game by setting up the appropriate card containers and views
   */
  private PlayerView createPlayerView(ImmutablePlayer player) {
    CardGroupContainer playerInHand = new CardGroupContainer(translator.translateToUserSpace("hand"), new VBox(10));
    CardGroupContainer playerInPlay = new CardGroupContainer(translator.translateToUserSpace("InPlay"), new VBox(10));
    CardGroupContainer opponentInHand = new CardGroupContainer(translator.translateToUserSpace("hand"), new VBox(10));
    CardGroupContainer opponentInPlay = new CardGroupContainer(translator.translateToUserSpace("InPlay"), new VBox(10));
    PlayerView playerView = new PlayerView(playerInHand, playerInPlay, opponentInHand, opponentInPlay, player);
    gameMainScene.addInactivePlayerView(playerView.getInactivePlayerView(), player.id());
    gameMainScene.addActivePlayerView(playerView.getActivePlayerView(), player.id());
    return playerView;

  }


  private CardGroupView createGroupView(ImmutableCardGroup group) {
    return switch (group.type()) {
      case STACK -> cardGroupFactory.createCardStack(group.id());
      default -> cardGroupFactory.createCardSequence(group.id());
    };
  }

  private CardView createCardView(ImmutableCard card) {
    CardView cardView = cardFactory.createCardView(card.suit().name(), card.value().ordinal() + 1,
        card.id());
    cardMap.put(card, cardView);
    return cardView;
  }
}

