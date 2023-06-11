package oogasalad.runner.model.condition;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.group.CardGroup;
/*
checks that a set has all the same values but different suits
 */
public class MeldCondition extends GroupCondition {
  public MeldCondition(Map<String, String> params, Board board) {
    super(params, board);
  }
  @Override
  protected boolean isValid(CardGroup group) { //checks validity for meld in gin rummy
    if (group.size() < 3 || group.size() > 4) {
      return false;
    }

    int value = group.cardAt(0).value().ordinal();

    for(int i=1; i<group.size(); i++){
      Card card=group.cardAt(i);
      if(card.value().ordinal()!=value){
        return false;
      }
    }
    return true;
  }

  @Override
  public TranslationPackage description() {
    return new TranslationPackage("Empty", getTag());
  }
}
