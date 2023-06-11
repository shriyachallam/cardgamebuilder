package oogasalad.runner.model.condition;

import static oogasalad.runner.model.card.StandardCard.StandardSuit.CLUB;
import static oogasalad.runner.model.card.StandardCard.StandardValue.ACE;
import static oogasalad.runner.model.card.StandardCard.StandardValue.THREE;
import static oogasalad.runner.model.card.StandardCard.StandardValue.TWO;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.StandardCard;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.card.group.ConcreteCardGroup;
import org.junit.jupiter.api.Test;

class GroupConditionTest {
  @Test
  void test() {
    Board board = new ConcreteBoard(() -> {});
    CardGroup group1a = new ConcreteCardGroup("test1",  CardGroup.ViewType.SEQUENCE, CardGroup.Direction.FACE_UP );
    CardGroup group1b = new ConcreteCardGroup("test1", CardGroup.ViewType.SEQUENCE, CardGroup.Direction.FACE_UP);
    CardGroup group2 = new ConcreteCardGroup("test2", CardGroup.ViewType.SEQUENCE, CardGroup.Direction.FACE_UP);
    board.addGroup(group1a);
    board.addGroup(group1b);
    board.addGroup(group2);
    Card card1a = new StandardCard(CLUB, ACE);
    Card card1b = new StandardCard(CLUB, TWO);
    Card card2 = new StandardCard(CLUB, THREE);
    GroupCondition groupCondition1 = new EmptyCondition(Map.of("group_tag", "test1"), board);
    GroupCondition groupCondition2 = new EmptyCondition(Map.of("group_tag", "test2"), board);
    assertTrue(groupCondition1.isValid());
    assertTrue(groupCondition2.isValid());
    group1a.add(card1a);
    assertFalse(groupCondition1.isValid());
    group1b.add(card1b);
    assertFalse(groupCondition1.isValid());
    assertTrue(groupCondition2.isValid());
    group2.add(card2);
    assertFalse(groupCondition2.isValid());
  }

}