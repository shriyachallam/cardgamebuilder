package oogasalad.runner.model.condition;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.StandardCard;
import oogasalad.runner.model.card.StandardCard.StandardSuit;
import oogasalad.runner.model.card.StandardCard.StandardValue;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.card.group.ConcreteCardGroup;
import org.junit.jupiter.api.Test;

class SuitConditionTest {
  @Test
  void test() {

    SuitCondition condition = new SuitCondition(Map.of("group_tag", "group"), null);
    Board board = new ConcreteBoard();
    CardGroup group = new ConcreteCardGroup();
    board.addGroup(group);
    assertTrue(condition.isValid(group));
    Card card1 = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    Card card2 = new StandardCard(StandardSuit.CLUB, StandardValue.TWO);
    Card card3 = new StandardCard(StandardSuit.HEART, StandardValue.ACE);
    Card card4 = new StandardCard(StandardSuit.SPADE, StandardValue.ACE);
    group.add(card1);
    assertTrue(condition.isValid(group));
    group.add(card2);
    assertTrue(condition.isValid(group));
    group.add(card3);
    assertFalse(condition.isValid(group));
    group.add(card4);
    assertFalse(condition.isValid(group));
    group.remove(card1);
    group.remove(card2);
    group.remove(card3);
    assertTrue(condition.isValid(group));
  }

}