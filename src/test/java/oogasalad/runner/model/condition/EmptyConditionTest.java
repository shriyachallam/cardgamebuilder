package oogasalad.runner.model.condition;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import oogasalad.runner.model.card.StandardCard;
import oogasalad.runner.model.card.StandardCard.StandardSuit;
import oogasalad.runner.model.card.StandardCard.StandardValue;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.card.group.ConcreteCardGroup;
import org.junit.jupiter.api.Test;

class EmptyConditionTest {
  @Test
  void test() {
    CardGroup group = new ConcreteCardGroup("test", CardGroup.ViewType.SEQUENCE, CardGroup.Direction.FACE_UP);
    EmptyCondition condition = new EmptyCondition(Map.of("group_tag", ""), null);
    assertTrue(condition.isValid(group));
    group.add(new StandardCard(StandardSuit.CLUB, StandardValue.ACE));
    assertFalse(condition.isValid(group));
  }

}