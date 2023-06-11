package oogasalad.runner.model.action.computer;

import java.util.Map;
import oogasalad.runner.model.board.Board;
import java.util.Random;
/*
for each player's hand, randomly remove 1 card
 */
public class RandomRemove implements ComputerAction{

  private Board board;
  private int amount;

  public RandomRemove(Map<String, String> params, Board board){
    this.board = board;
    amount = Integer.parseInt(params.get("amount"));
  }

  @Override
  public void act() {
    board.groups().stream().filter(group -> group.tag().equals("hand")).forEach(
      group -> {
        for(int i = 0; i < amount; i++){
          Random random = new Random();
          int cardIndex = random.nextInt(group.size());
          group.removeByIndex(cardIndex);
        }
      }
    );
  }
}
