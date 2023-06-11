package oogasalad.runner.model.condition;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.StandardCard;
import oogasalad.runner.model.card.StandardCard.StandardSuit;
import oogasalad.runner.model.card.StandardCard.StandardValue;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.card.group.ConcreteCardGroup;
import org.junit.jupiter.api.Test;

public class MeldConditionTest {
@Test
  void test() {
  CardGroup group = new ConcreteCardGroup("test", CardGroup.ViewType.SEQUENCE, CardGroup.Direction.FACE_UP);
  MeldCondition condition = new MeldCondition(Map.of("group_tag", "set"), null);
  group.add(new StandardCard(StandardSuit.CLUB, StandardValue.ACE));
  group.add(new StandardCard(StandardSuit.SPADE, StandardValue.ACE));
  group.add(new StandardCard(StandardSuit.HEART, StandardValue.ACE));
  assertTrue(condition.isValid(group));

//  // false if meld has suit repetition
//  Card card = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
//  group.add(card);
//  assertFalse(condition.isValid(group));
//  group.remove(card);

  // false if meld has inconsistent value
  Card card2 = new StandardCard(StandardSuit.DIAMOND, StandardValue.TWO);
  group.add(card2);
  assertFalse(condition.isValid(group));
  }
}
