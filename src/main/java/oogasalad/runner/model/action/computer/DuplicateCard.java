package oogasalad.runner.model.action.computer;

import java.util.Map;
import java.util.Random;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.StandardCard;
import oogasalad.runner.model.card.StandardCard.StandardSuit;
import oogasalad.runner.model.card.StandardCard.StandardValue;
/*
for each player's hand, pick a random card and duplicate it
 */
public class DuplicateCard implements ComputerAction{

  private Board board;

  public DuplicateCard(Map<String, String> params, Board board){
    this.board = board;
  }

  @Override
  public void act() {
    board.groups().stream().filter(group -> group.tag().equals("hand")).forEach(
        group -> {
          Random random = new Random();
          int cardIndex = random.nextInt(group.size());
          Card card = group.cardAt(cardIndex);
          Card duplicate = new StandardCard((StandardSuit) card.suit(), (StandardValue) card.value());
          group.add(duplicate);
        }
    );
  }
}
