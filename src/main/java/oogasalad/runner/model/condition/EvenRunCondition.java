package oogasalad.runner.model.condition;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.group.CardGroup;
/*
checks that there is a consecutive sequence of even cards with the same incrementation and all the same suit
 */
public class EvenRunCondition extends GroupCondition{

  public EvenRunCondition(Map<String, String> params, Board board){
    super(params, board);
  }

  @Override
  protected boolean isValid(CardGroup pile) {
    if(pile.size() < 3 || pile.size() > 4) return false;

    int value = pile.cardAt(0).value().ordinal();
    String suit = pile.cardAt(0).suit().name();

    for(int i=1; i<pile.size() - 1; i++){
      Card current = pile.cardAt(i);
      int curVal = current.value().ordinal();
      String curSuit = current.suit().name();
      Card next = pile.cardAt(i + 1);
      int nextVal = next.value().ordinal();
      String nextSuit = next.suit().name();

      if(nextVal - curVal != curVal - value || ! nextSuit.equals(suit)
          || ! curSuit.equals(suit) || value % 2 != 0 ){
        return false;
      }
    }

    return true;
  }

  @Override
  public TranslationPackage description() {
    return null;
  }
}
