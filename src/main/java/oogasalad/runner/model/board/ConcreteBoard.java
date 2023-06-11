package oogasalad.runner.model.board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oogasalad.runner.controller.observer.BoardObserver;
import oogasalad.runner.model.action.player.PlayerAction;
import oogasalad.runner.model.board.backup.BoardBackup;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.condition.Condition;
import oogasalad.runner.model.exception.CardNotFoundException;
import oogasalad.runner.model.exception.NonexistentIDException;
import oogasalad.runner.model.player.Player;

/**
 * implementation of the Board interface. Defines a game board for a card game.
 *
 * @author Delali Cudjoe
 */
public class ConcreteBoard implements Board {

  private final Set<CardGroup> boardGroups = new HashSet<>();
  private final List<Player> players = new ArrayList<>();
  private final List<Runnable> callbacks = new ArrayList<>();
  private String phaseDescription;
  private List<PlayerAction> allowedMoves = new ArrayList<>();
  private List<Condition> phaseCondition = new ArrayList<>();
  private int phaseTransfers;
  private Runnable gameStep;
  private BoardBackup backup;


  public ConcreteBoard() {
  }
  public ConcreteBoard(Runnable gameStep) {
    this.gameStep = gameStep;
  }


  public void setGameStep(Runnable gameStep) {
    this.gameStep = gameStep;
  }

  @Override
  public void addGroup(CardGroup group) {
    boardGroups.add(group);
    notifyObservers();
  }

  @Override
  public void addPlayer(Player player) {
    players.add(player);
    notifyObservers();
  }

  @Override
  public Set<Card> cards() {
    Set<Card> cards = new HashSet<>();
    boardGroups.forEach(group -> cards.addAll(group.stream().toList()));
    players.forEach(player -> cards.addAll(player.hand().stream().toList()));
    return cards;
  }

  @Override
  public List<Player> players() {
    return players;
  }

  @Override
  public Set<Selectable> selectables() {
    Set<Selectable> selectables = new HashSet<>();
    players.forEach(player -> selectables.add(player.hand()));
    selectables.addAll(boardGroups);
    selectables.addAll(this.cards());
    return selectables;
  }

  @Override
  public Set<CardGroup> groups() {
    Set<CardGroup> groups = new HashSet<>();
    groups.addAll(boardGroups);
    players.forEach(player -> groups.add(player.hand()));
    return groups;
  }

  @Override
  public void transfer(CardGroup source, CardGroup destination, int amount) {
    for (int i = 0; i < amount; i++) {
      Card card = source.draw();
      destination.add(card);
      phaseTransfers++;
      notifyObservers();
    }
  }

  @Override
  public void moveCard(Card card, CardGroup destination) throws CardNotFoundException {
    //search for the card in the groups and the player hands, then move it
    Set<CardGroup> allGroups = new HashSet<>(this.boardGroups);
    allGroups.addAll(this.players.stream().map(player -> player.hand()).toList());
    allGroups.stream().filter(group -> group.contains(card)).forEach(group -> {
      group.remove(card);
      phaseTransfers++;
    });
    destination.add(card);
    notifyObservers();
  }

  @Override
  public List<PlayerAction> allowedMoves() {
    return allowedMoves;
  }

  @Override
  public void setAllowedMoves(List<PlayerAction> actions) {
    allowedMoves = actions;
    phaseTransfers = 0;
    notifyObservers();
  }

  @Override
  public List<Condition> phaseConditions() {
    return phaseCondition;
  }

  @Override
  public void setPhaseConditions(List<Condition> phaseCondition) {
    this.phaseCondition = phaseCondition;
    notifyObservers();
  }

  public void setPhaseDescription(String description) {
    this.phaseDescription = description;
  }

  public String getPhaseDescription() {
    return phaseDescription;
  }

  @Override
  public int getPhaseTransfers() {
    return phaseTransfers;
  }

  @Override
  public void register(BoardObserver observer) {
    observer.linkBoard(this);
    callbacks.add(observer.getBoardCallback());
  }


  @Override
  public void saveBackup() {
    backup = new BoardBackup(this);
  }

  @Override
  public void revertState() {
    List<CardGroup> setsToRemove = new ArrayList<>();
    phaseTransfers = 0;
    for (CardGroup group : groups()) {
      try {
        group.clear();
        group.addAll(backup.getSavedCards(group.id()));
      } catch (NonexistentIDException nonexistentIDException) {
        setsToRemove.add(group);
      }
    }
    boardGroups.removeAll(setsToRemove);
    notifyObservers();
  }

  public void notifyObservers() {
    callbacks.forEach(Runnable::run);
  }

  @Override
  public void select(int selectId) {
    selectables().stream().filter(
        selectable -> selectable.id() == selectId
    ).forEach(selected -> {
      selected.select();
      if (selected instanceof Card card) {
        boardGroups.stream().filter(group -> group.contains(card))
            .forEach(group -> group.deselect());
      } else if (selected instanceof CardGroup group) {
        cards().stream().filter(card -> group.contains(card))
            .forEach(card -> card.deselect());
      }
    });
    notifyObservers();
  }

  @Override
  public void deselect(int selectId) {
    selectables().stream().filter(selectable -> selectable.id() == selectId)
        .forEach(Selectable::deselect);
    notifyObservers();

  }

  @Override
  public void endPhase() {
    gameStep.run();
  }
}